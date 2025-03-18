package renko.jiang.campus_life_guide.controller.chat;


import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_life_guide.pojo.dto.MessageDTO;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.ChatVO;
import renko.jiang.campus_life_guide.pojo.vo.MessageVO;
import renko.jiang.campus_life_guide.pojo.vo.UserInfoVO;
import renko.jiang.campus_life_guide.service.ChatRoomService;


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

    /**
     * 获取当前用户的所有聊天
     */
    @GetMapping("/list")
    public Result<List<ChatVO>> list(){
        return chatRoomService.queryChatRoomsOfCurrentUser();
    }

    /**
     * 获取指定聊天的所有消息
     */
    @GetMapping("/messages/{chatId}")
    public Result<List<MessageVO>> listMessages(@PathVariable Long chatId){
        return chatRoomService.queryMessagesByChatId(chatId);
    }

    /**
     * 处发送消息
     */
    @PostMapping("/message")
    public Result<Message> sendMessage(@RequestBody MessageDTO messageDTO){
        if(messageDTO == null){
            return Result.error("消息为空");
        }
        if(messageDTO.getChatId() == null){
            return Result.error("聊天室id不能为空");
        }
        if(messageDTO.getContent() == null){
            return Result.error("消息内容不能为空");
        }
        return chatRoomService.sendMessage(messageDTO);
    }

    /**
     * 群聊成员
     */
    @GetMapping("/members/{chatId}")
    public Result<List<UserInfoVO>> listMembers(@PathVariable Long chatId){
        return chatRoomService.queryChatRoomMembers(chatId);
    }

    /**
     * 读消息
     */
    @PostMapping("/read/{chatId}")
    public Result readMessage(@PathVariable Long chatId, @RequestBody Map<String, Long> requestBody){
        Long lastMessageId = requestBody.get("lastMessageId");
        return chatRoomService.readMessage(chatId, lastMessageId);
    }

}

