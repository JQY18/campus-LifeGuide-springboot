package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (UserChat)实体类
 *
 * @author makejava
 * @since 2025-03-17 22:25:53
 */

@Data
public class UserChat implements Serializable {
    private static final long serialVersionUID = -54320753785150245L;

    private Long id;

    private Integer userId;

    private Long chatId;

    private String type;
/**
     * 最后已读的message id
     */
    private Long lastRead;

    private LocalDateTime createdTime;

}

