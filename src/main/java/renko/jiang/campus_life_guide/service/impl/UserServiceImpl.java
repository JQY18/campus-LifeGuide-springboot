package renko.jiang.campus_life_guide.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.mapper.*;
import renko.jiang.campus_life_guide.pojo.dto.LoginDTO;
import renko.jiang.campus_life_guide.pojo.dto.UserInfoDTO;
import renko.jiang.campus_life_guide.pojo.entity.ChatRoom;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.UserInfoVO;
import renko.jiang.campus_life_guide.service.UserService;
import renko.jiang.campus_life_guide.utils.RedisUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 86132
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserChatMapper userChatMapper;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${upload.path}")
    private String path;

    @Override
    public User login(LoginDTO loginDTO) {
        User user = userMapper.queryUser(loginDTO.getUsername());
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public Result register(LoginDTO loginDTO) {
        log.info("用户注册：{}", loginDTO);

        // 邮箱
        String email = loginDTO.getEmail();
        // 验证码
        String code = loginDTO.getCode();
        // 验证码校验
        String key = redisUtil.buildKey(RedisUtil.VERIFICATION_CODE_KEY, email);
        Object verificationCode = redisUtil.get(key);

        if (verificationCode == null || !verificationCode.equals(code)) {
            return Result.error("验证码错误");
        }

        try {
            userMapper.addUser(loginDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户名已存在");
        }

        redisUtil.delete(key);
        return Result.success();
    }

    @Override
    public UserInfoVO getUserInfoById(Integer userId) {
        UserInfoVO userInfo = userMapper.getUserInfoById(userId);
        //查询用户主页的帖子收藏总数
        int collections = postMapper.getUserCollectionsCount(userId);
        userInfo.setCollections(collections);
        return userInfo;
    }

    @Override
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO.getAvatar() != null) {
            String imageUrl = handleSingleFileUpload(userInfoDTO.getAvatar());
            userInfoDTO.setImageUrl(imageUrl);
        }
        userMapper.updateUserInfo(userInfoDTO);
    }

    @Override
    public void updateAvatar(Integer id, MultipartFile avatar) {
        String url = handleSingleFileUpload(avatar);
        userMapper.updateAvatar(id, url);
    }

    //修改密码
    @Override
    public void updatePassword(Integer id, String currentPassword, String newPassword) {
        int update = userMapper.updatePassword(id, currentPassword, newPassword);
        if (update == 0) {
            throw new RuntimeException("密码错误");
        }
    }

    @Override
    public Result deleteUser(Integer id) {
        int update = userMapper.deleteUser(id);
        if (update == 0) {
            return Result.error("删除失败");
        } else {
            return Result.success();
        }
    }

    @Override
    public Result<PageResult<UserInfoVO>> page(UserInfoDTO userInfoDTO) {
        PageResult<UserInfoVO> pageResult = new PageResult<>();

        userInfoDTO.setPageNo(userInfoDTO.getPageNo());
        userInfoDTO.setPageSize(userInfoDTO.getPageSize());

        int start = (userInfoDTO.getPageNo() - 1) * userInfoDTO.getPageSize();

        User user = new User();
        BeanUtils.copyProperties(userInfoDTO, user);

        int total = userMapper.countByCondition(user);
        if (total == 0) {
            return Result.success(pageResult);
        }

        List<User> users = userMapper.pageQueryByCondition(user, start, userInfoDTO.getPageSize());

        List<UserInfoVO> userInfoVOS = new ArrayList<>();
        //BeanUtils.copyProperties(users, userInfoVOS);

        for (User user1 : users) {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user1, userInfoVO);
            userInfoVOS.add(userInfoVO);
        }

        pageResult.setRecords(userInfoVOS);

        pageResult.setTotal(total);
        return Result.success(pageResult);
    }

    /**
     * 加好友
     *
     * @param friendId
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addFriend(Integer friendId) {
        //添加好友实际上就是添加一个聊天室（私聊），在chat表中添加一条记录,
        //并在use_chat表中分别将自己的id和好友的id都绑定这个private聊天室的id
        //1.获取当前用户的id
        Integer userId = UserContextHolder.getUserId();
        //2.判断当前用户是否已经是好友
        if (isFriend(userId, friendId)) {
            return Result.error("已发送好友申请");
        }
        //3.如果不是好友，则添加好友
        //3.1.添加private聊天室,并返回聊天室id
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType("private");
        long insert = chatRoomMapper.addChatRoom(chatRoom);

        if (insert == 0) {
            throw new RuntimeException("添加好友失败");
        }
        //3.2.将当前用户id和好友id分别，与private聊天室 绑定到user_chat表中
        int count = userChatMapper.addFriend(userId, friendId, chatRoom.getId());

        if (count != 2) {
            throw new RuntimeException("添加好友失败");
        }
        return Result.success();
    }

    /**
     * 删除好友
     *
     * @param friendId
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteFriend(Integer friendId) {
        //删除好友关系 == 删除两人的private聊天室
        //1.获取当前用户的id
        Integer userId = UserContextHolder.getUserId();
        if (userId.equals(friendId)) {
            return Result.error("不能删除自己");
        }
        //查找两人的聊天室id
        Long chatId = userChatMapper.queryChatIdBetweenFriends(userId, friendId);
        if (chatId == null) {
            return Result.error("不是好友关系");
        }
        //2.删除user_chat表中，chatId对应的记录
        long count = userChatMapper.deleteByChatId(chatId);
        if (count != 2) {
            throw new RuntimeException("删除好友失败");
        }
        //3.删除chatId对应的聊天室
        long count2 = chatRoomMapper.deleteChatRoomByChatId(chatId);
        if (count2 == 0) {
            throw new RuntimeException("删除好友失败");
        }
        //4.删除聊天室的所有消息
        long count3 = messageMapper.deleteByChatId(chatId);
        if (count3 == 0) {
            throw new RuntimeException("删除好友失败");
        }
        return Result.success();
    }


    /**
     * 判断当前用户是否已经是好友
     *
     * @param friendId
     * @return
     */
    @Override
    public Result<Boolean> checkFriend(Integer friendId) {
        Integer userId = UserContextHolder.getUserId();

        if (userId.equals(friendId)) {
            return Result.success(false);
        }

        if (isFriend(userId, friendId)) {
            return Result.success(true);
        }
        return Result.success(false);
    }


    /**
     * 检查邮箱是否已经注册过
     *
     * @param email
     * @return
     */
    @Override
    public boolean existUserByEmail(String email) {
        return userMapper.existUserByEmail(email) > 0;
    }

    /**
     * 判断当前用户是否已经是好友
     *
     * @param userId
     * @param friendId
     * @return
     */
    private boolean isFriend(Integer userId, Integer friendId) {
        //判断当前用户是否已经是好友
        //只要user_chat表中存在（user_id,chat_id） 和 （friend_id,chat_id）则是好友
        Long chatId = userChatMapper.queryChatIdBetweenFriends(userId, friendId);
        return chatId != null;
    }

    //处理图片上传并且返回图片的静态资源映射地址
    public String handleSingleFileUpload(MultipartFile avatar) {
        try {
            // 生成UUID
            String uuid = UUID.randomUUID().toString();
            // 文件扩展名
            String originalFilename = avatar.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构建新的文件名
            String newFileName = uuid + fileExtension;

            // 构建文件路径
            Path path = Paths.get(this.path, newFileName);
            // 将MultipartFile写入文件系统
            Files.write(path, avatar.getBytes());

            // 收集该帖子id对应的图片的静态资源映射地址
            return "http://localhost:8080/image/" + newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

}
