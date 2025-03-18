package renko.jiang.campus_life_guide.controller.chat;

import lombok.Data;

/**
 * @author 86132
 */
@Data
public class ChatMessage {
    private Long sendTo;
    private Long chatId;
    private Integer senderId;
    private String senderAvatar;
    private String senderName;
    private String content;
    private String createdTime;
}
