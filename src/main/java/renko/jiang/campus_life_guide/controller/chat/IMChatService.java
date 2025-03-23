package renko.jiang.campus_life_guide.controller.chat;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import renko.jiang.campus_life_guide.pojo.dto.MessageDTO;
import renko.jiang.campus_life_guide.service.ChatRoomService;

/**
 * @author 86132
 */
@Slf4j
@Service
public class IMChatService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private ChatRoomService chatRoomService;

    public void sendGroupMessage(ChatMessage chatMessage) {
        //
        log.info("renko.jiang.campus_life_guide.controller.chat.IMChatService:群聊消息:{}",chatMessage);
        // 发送消息给特定群聊频道
        simpMessagingTemplate.convertAndSend("/topic/group/" + chatMessage.getSendTo(), chatMessage);
        //持久化
        saveMessage(chatMessage);
    }

    public void privateMessage(ChatMessage chatMessage) {
        //
        log.info("renko.jiang.campus_life_guide.controller.chat.IMChatService:私聊消息:{}",chatMessage);
        // 发送消息给特定用户
        // "/user/" + chatMessage.getSendTo() + "/private"
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSendTo().toString(), "/private", chatMessage);
        //持久化
        saveMessage(chatMessage);
    }

    private void saveMessage(ChatMessage chatMessage) {
        //先检查该聊天是否被删除
        if(!chatRoomService.existChatRoom(chatMessage.getChatId())){
            return;
        }
        chatRoomService.saveMessage(chatMessage);
    }
}
