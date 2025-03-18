package renko.jiang.campus_life_guide.controller.chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 86132
 */
@Controller
public class IMChatController {

    @Autowired
    private IMChatService imChatService;

    @MessageMapping("/group-message")
    public void sendGroupMessage(ChatMessage chatMessage) {
        // 这里需要根据接收者的ID发送群聊消息
        // 可以使用SimpMessagingTemplate来发送消息给特定
        imChatService.sendGroupMessage(chatMessage);
    }

    @MessageMapping("/private-message")
    public void privateMessage(ChatMessage chatMessage) throws Exception {
        // 这里需要根据接收者的ID发送私聊消息
        // 可以使用SimpMessagingTemplate来发送消息给特定用户
        imChatService.privateMessage(chatMessage);
    }
}
