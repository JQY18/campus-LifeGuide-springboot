package renko.jiang.campus_life_guide.controller.chat;


import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_life_guide.pojo.dto.GroupChatDTO;
import renko.jiang.campus_life_guide.pojo.dto.MessageDTO;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.ChatVO;
import renko.jiang.campus_life_guide.pojo.vo.GroupMember;
import renko.jiang.campus_life_guide.pojo.vo.MessageVO;
import renko.jiang.campus_life_guide.service.ChatRoomService;
import renko.jiang.campus_life_guide.service.UserOnlineStatusService;

import java.util.List;
import java.util.Map;


/**
 * (ChatRoom)表控制层
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    /**
     * 服务对象
     */
    @Resource
    private ChatRoomService chatRoomService;

    @Autowired
    private UserOnlineStatusService userOnlineStatusService;

    /**
     * 获取当前用户的所有聊天
     */
    @GetMapping("/list")
    public Result<List<ChatVO>> list() {
        return chatRoomService.queryChatRoomsOfCurrentUser();
    }

    /**
     * 获取指定聊天的所有消息
     */
    @GetMapping("/messages/{chatId}")
    public Result<List<MessageVO>> listMessages(@PathVariable Long chatId) {
        return chatRoomService.queryMessagesByChatId(chatId);
    }

    /**
     * 发送消息
     */
    @PostMapping("/message")
    public Result<Message> sendMessage(@RequestBody MessageDTO messageDTO) {
        if (messageDTO == null) {
            return Result.error("消息为空");
        }
        if (messageDTO.getChatId() == null) {
            return Result.error("聊天室id不能为空");
        }
        if (messageDTO.getContent() == null) {
            return Result.error("消息内容不能为空");
        }
        return chatRoomService.sendMessage(messageDTO);
    }

    /**
     * 群聊成员
     */
    @GetMapping("/members/{chatId}")
    public Result<List<GroupMember>> listMembers(@PathVariable Long chatId) {
        return chatRoomService.queryChatRoomMembers(chatId);
    }

    /**
     * 读消息
     */
    @PostMapping("/read/{chatId}")
    public Result readMessage(@PathVariable Long chatId, @RequestBody Map<String, Long> requestBody) {
        Long lastMessageId = requestBody.get("lastMessageId");
        return chatRoomService.readMessage(chatId, lastMessageId);
    }


    /**
     * 创建群聊
     *
     * @param groupChatDTO 群聊信息
     */
    @Operation(summary = "创建群聊")
    @PostMapping("/group/add")
    public Result<Long> addGroupChatRoom(@RequestBody GroupChatDTO groupChatDTO) {
        if (groupChatDTO == null || groupChatDTO.getUserIds().isEmpty() || StrUtil.isBlank(groupChatDTO.getName())) {
            return Result.error("用户列表不能为空");
        }
        return chatRoomService.addGroupChatRoom(groupChatDTO);
    }

    /**
     * 解散（删除）群聊
     *
     * @param chatId 群聊id
     */
    @Operation(summary = "解散群聊")
    @DeleteMapping("/group/delete/{chatId}")
    public Result deleteGroupChatRoom(@PathVariable Long chatId) {
        return chatRoomService.deleteByChatId(chatId);
    }

    /**
     * 退出群聊
     */
    @Operation(summary = "退出群聊")
    @DeleteMapping("/group/exit/{chatId}")
    public Result exitGroupChatRoom(@PathVariable Long chatId) {
        return chatRoomService.exitGroupChat(chatId);
    }


    /**
     * 用户在线状态
     */
    @GetMapping("/online/{userId}")
    public Result<Boolean> isOnline(@PathVariable Integer userId) {
        return Result.success(userOnlineStatusService.isOnline(userId));
    }
}

