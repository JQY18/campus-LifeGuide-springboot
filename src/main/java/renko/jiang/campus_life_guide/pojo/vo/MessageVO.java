package renko.jiang.campus_life_guide.pojo.vo;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class MessageVO {
    private Long id;
    private Integer senderId;
    private String senderName;
    private String senderAvatar;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
