package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (ChatRoom)实体类
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */

@Data
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 849196263740933785L;

    private Long id;

    private String name;

    private String avatar;

    private String type;

    private LocalDateTime createdTime;

}

