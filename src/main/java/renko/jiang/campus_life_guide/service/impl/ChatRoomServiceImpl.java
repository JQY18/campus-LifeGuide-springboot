package renko.jiang.campus_life_guide.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.controller.chat.ChatMessage;
import renko.jiang.campus_life_guide.mapper.ChatRoomMapper;
import renko.jiang.campus_life_guide.mapper.MessageMapper;
import renko.jiang.campus_life_guide.mapper.UserChatMapper;
import renko.jiang.campus_life_guide.mapper.UserMapper;
import renko.jiang.campus_life_guide.pojo.dto.GroupChatDTO;
import renko.jiang.campus_life_guide.pojo.dto.MessageDTO;
import renko.jiang.campus_life_guide.pojo.entity.ChatRoom;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.entity.UserChat;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.ChatVO;
import renko.jiang.campus_life_guide.pojo.vo.GroupMember;
import renko.jiang.campus_life_guide.pojo.vo.MessageVO;
import renko.jiang.campus_life_guide.service.ChatRoomService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * (ChatRoom)表服务实现类
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */

@Slf4j
@Service("chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {
    @Resource
    private ChatRoomMapper chatRoomMapper;

    @Resource
    private UserChatMapper userChatMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询指定聊天室中的所有消息
     *
     * @param chatId
     * @return
     */
    @Override
    public Result<List<MessageVO>> queryMessagesByChatId(Long chatId) {
        if (chatId == null) {
            return Result.error("聊天室id不能为空");
        }

        List<MessageVO> messageVOS = new ArrayList<>();

        List<Message> messages = messageMapper.queryMessagesByChatId(chatId);

        // 如果聊天室中没有消息，直接返回
        if (CollectionUtil.isEmpty(messages)) {
            return Result.success(messageVOS);
        }

        //查找每个消息的发送者信息
        List<Map<String, Object>> senders = userChatMapper.querySenders(messages);
        Map<Long, Map<String, Object>> sendersMap = senders.stream().collect(Collectors.toMap(
                row -> (Long) row.get("id"),
                Function.identity(),
                (oldValue, newValue) -> oldValue
        ));

        for (Message message : messages) {
            MessageVO messageVO = BeanUtil.copyProperties(message, MessageVO.class);
            Long messageId = message.getId();

            Map<String, Object> map = sendersMap.get(messageId);
            if (map != null) {
                messageVO.setSenderName((String) map.getOrDefault("nickname", ""));
                messageVO.setSenderAvatar((String) map.getOrDefault("avatar", ""));
            }
            messageVOS.add(messageVO);
        }

        return Result.success(messageVOS);
    }


    /**
     * 查询当前用户的所有聊天室
     *
     * @return
     */
    @Override
    public Result<List<ChatVO>> queryChatRoomsOfCurrentUser() {
        Integer userId = UserContextHolder.getUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        List<ChatVO> chatVOS = new ArrayList<>();
        //先查找用户的聊天室信息
        List<UserChat> userChats = userChatMapper.queryChatRoomsOfCurrentUser(userId);
        //如果没有聊天室，直接返回
        if (CollectionUtil.isEmpty(userChats)) {
            return Result.success(chatVOS);
        }

        //根据userChats中的last_read字段，计算对应的chat_id未读消息数量
        List<Map<String, Object>> unreadCountMapList = messageMapper.queryUnreadCount(userChats);

        Map<Long, Long> unreadCountMap = new HashMap<>();

        //查询结果不为空，将查询结果转换为map
        if (CollectionUtil.isNotEmpty(unreadCountMapList)) {
            unreadCountMap = unreadCountMapList.stream().collect(Collectors.toMap(
                    row -> (Long) row.get("chatId"),
                    row -> (Long) row.get("messageCount")
            ));
        }


        //根据查找到的聊天室的ids，查找聊天室信息
        List<Long> chatIds = userChats.stream().map(UserChat::getChatId).toList();
        List<ChatRoom> chatRooms = chatRoomMapper.queryByIds(chatIds);

        //私聊列表展示的name和avatar是对方用户的nickname和avatar
        //查询私聊的用户信息
        List<UserChat> privateChatUsers = userChats.stream()
                .filter(userChat -> "private".equals(userChat.getType()))
                .toList();

        Map<Long, Map<String, Object>> privateChatUsersMap = null;
        if (CollectionUtil.isNotEmpty(privateChatUsers)) {
            List<Map<String, Object>> privateChatUsersResult = userChatMapper.queryPrivateChatUser(privateChatUsers, userId);
            privateChatUsersMap = privateChatUsersResult.stream().collect(Collectors.toMap(
                    row -> (Long) row.get("chatId"),
                    Function.identity(),
                    (oldValue, newValue) -> oldValue
            ));
        }

        //根据chatIds查找聊天中的最后一条消息
        List<Message> lastMessage = messageMapper.queryLastMessage(chatIds);

        Map<Long, Message> lastMessageMap = lastMessage.stream().collect(Collectors.toMap(
                Message::getChatId,
                Function.identity(),
                (oldValue, newValue) -> oldValue
        ));


        //整合数据
        for (ChatRoom chatRoom : chatRooms) {
            ChatVO chatVO = new ChatVO();
            BeanUtil.copyProperties(chatRoom, chatVO);
            chatVO.setUnreadCount(unreadCountMap.getOrDefault(chatRoom.getId(), 0L).intValue());
            chatVO.setLastMessage(lastMessageMap.get(chatRoom.getId()));
            if ("private".equals(chatRoom.getType())) {
                if (CollectionUtil.isNotEmpty(privateChatUsersMap)) {
                    Map<String, Object> privateChatUser = privateChatUsersMap.get(chatRoom.getId());
                    chatVO.setUserId((Integer) privateChatUser.get("id"));
                    chatVO.setName((String) privateChatUser.get("nickname"));
                    chatVO.setAvatar((String) privateChatUser.get("avatar"));
                }
            }
            chatVOS.add(chatVO);
        }
        return Result.success(chatVOS);
    }


    /**
     * 发送消息
     *
     * @param messageDTO
     * @return
     */
    @Transactional(rollbackFor = SQLException.class)
    @Override
    public Result<Message> sendMessage(MessageDTO messageDTO) {
        Message message = BeanUtil.copyProperties(messageDTO, Message.class);
        //设置发送者id
        message.setSenderId(UserContextHolder.getUserId());
        Integer insert = messageMapper.insert(message);
        if (insert == null || insert == 0) {
            return Result.error("发送失败");
        }
        //更新当前用户的最后已读的消息id
        log.info("messageId:{}", message.getId());
        Long messageId = message.getId();
        Integer update = userChatMapper.updateLastRead(messageId, UserContextHolder.getUserId(), message.getChatId());
        if (update == null || update == 0) {
            return Result.error("更新最后已读消息失败");
        }
        return Result.success(message);
    }

    /**
     * 查询聊天室成员
     *
     * @param chatId
     * @return
     */
    @Override
    public Result<List<GroupMember>> queryChatRoomMembers(Long chatId) {
        if (chatId == null) {
            return Result.error("请选择聊天室");
        }

        //查询聊天室成员
        List<UserChat> userChats = userChatMapper.queryChatRoomMembers(chatId);
        Map<Integer, String> userRoleMap = userChats.stream().collect(Collectors.toMap(
                UserChat::getUserId,
                UserChat::getRole,
                (oldValue, newValue) -> oldValue
        ));

        //userIds
        List<Integer> userIds = userChats.stream().map(UserChat::getUserId).toList();
        if (CollectionUtil.isEmpty(userIds)) {
            return Result.error("似乎没有群成员");
        }

        //查询用户信息
        List<User> users = userMapper.queryUserInfoByIds(userIds);
        //将用户信息转换为UserInfoVO
        List<GroupMember> gGroupMembers = users.stream().map(user -> {
            GroupMember gGroupMember = BeanUtil.copyProperties(user, GroupMember.class);
            //设置成员的角色
            gGroupMember.setRole(userRoleMap.get(user.getId()));
            return gGroupMember;
        }).toList();

        return Result.success(gGroupMembers);
    }

    @Override
    public Result readMessage(Long chatId, Long lastMessageId) {
        if (chatId == null || lastMessageId == null) {
            return Result.error("参数错误");
        }
        Integer userId = UserContextHolder.getUserId();
        if (userChatMapper.updateLastRead(lastMessageId, userId, chatId) > 0) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        Message message = BeanUtil.copyProperties(chatMessage, Message.class);

        Integer insert = messageMapper.insert(message);
        if (insert == null || insert == 0) {
            log.error("renko.jiang.campus_life_guide.service.impl.ChatRoomServiceImpl.saveMessage:保存消息失败");
        }
        //更新当前用户的最后已读的消息id
        log.info("messageId:{}", message.getId());
        Long messageId = message.getId();
        Integer update = userChatMapper.updateLastRead(messageId, message.getSenderId(), message.getChatId());
        if (update == null || update == 0) {
            log.error("renko.jiang.campus_life_guide.service.impl.ChatRoomServiceImpl.saveMessage:更新最后已读消息失败");
        }
    }


    /**
     * 创建群聊
     *
     * @param groupChatDTO 群聊信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> addGroupChatRoom(GroupChatDTO groupChatDTO) {
        //群主（当前登录用户）
        Integer ownerId = UserContextHolder.getUserId();

        //1.创建一个group聊天室，并返回chatId
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType("group");
        chatRoom.setName(groupChatDTO.getName());
        long insert = chatRoomMapper.addChatRoom(chatRoom);
        if (insert == 0) {
            throw new RuntimeException("创建群聊失败");
        }
        List<Integer> userIds = groupChatDTO.getUserIds();
        //2.将成员id添加到user_chat表values (...user_id,chat_id...)
        long insert1 = userChatMapper.addGroupChatRoom(ownerId, chatRoom.getId(), userIds);
        if (insert1 != userIds.size() + 1) {
            throw new RuntimeException("添加群聊失败");
        }

        //3.返回群聊id

        return Result.success(chatRoom.getId());
    }


    /**
     * 删除群聊
     *
     * @param chatId 群聊id
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteByChatId(Long chatId) {
        //只有群主才能删除群聊
        Integer userId = UserContextHolder.getUserId();
        //查询群聊的群主id
        Integer ownerId = userChatMapper.queryChatRoomOwner(chatId);
        if (ownerId == null || !ownerId.equals(userId)) {
            return Result.error("只有群主才能删除群聊");
        }
        //删除群聊 聊天室
        chatRoomMapper.deleteChatRoomByChatId(chatId);
        //删除群聊成员 关系
        userChatMapper.deleteByChatId(chatId);
        //删除群聊消息
        messageMapper.deleteByChatId(chatId);

        return Result.success("群聊解散成功");
    }

    @Override
    public Result exitGroupChat(Long chatId) {
        //获取当前用户id
        Integer userId = UserContextHolder.getUserId();
        //从user_chat表中删除该用户和群聊的关系
        if (userChatMapper.exitGroupChat(userId, chatId) > 0) {
            return Result.success();
        }
        return Result.error("退出群聊失败");
    }

    @Override
    public boolean existChatRoom(Long chatId) {
        return chatRoomMapper.existChatRoom(chatId) > 0;
    }


    @Transactional
    @Override
    public void createGroupChatForMatch(Integer ownerId, List<Integer> userIds, String name) {
        //1.创建一个group聊天室，并返回chatId
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType("group");
        chatRoom.setName(name + LocalDate.now());
        long insert = chatRoomMapper.addChatRoom(chatRoom);
        if (insert == 0) {
            throw new RuntimeException("创建群聊失败");
        }

        //2.将成员id添加到user_chat表values (...user_id,chat_id...)
        long insert1 = userChatMapper.addGroupChatRoom(ownerId, chatRoom.getId(), userIds);
        if (insert1 != userIds.size() + 1) {
            throw new RuntimeException("添加群聊失败");
        }
    }
}
