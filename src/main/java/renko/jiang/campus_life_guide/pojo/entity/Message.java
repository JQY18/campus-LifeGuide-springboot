package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (Message)实体类
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = -37124540077476376L;

    private Long id;

    private Long chatId;

    private Integer senderId;

    private String content;

    private LocalDateTime createdTime;

}

