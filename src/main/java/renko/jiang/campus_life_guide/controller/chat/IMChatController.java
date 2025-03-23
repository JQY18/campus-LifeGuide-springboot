package renko.jiang.campus_life_guide.controller.chat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author 86132
 */

@Slf4j
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
    public void privateMessage(ChatMessage chatMessage, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) throws Exception {
        // 这里需要根据接收者的ID发送私聊消息
        // 可以使用SimpMessagingTemplate来发送消息给特定用户
        log.info("renko.jiang.campus_life_guide.controller.chat.IMChatController.获取到的userId:{}", sessionAttributes.get("userId"));
        imChatService.privateMessage(chatMessage);
    }
}
