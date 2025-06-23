package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (UserMatch)实体类
 *
 * @author makejava
 * @since 2025-04-07 19:32:34
 */

@Data
public class UserMatch {

    private Integer id;

    private Integer userId;

    private Integer matchId;

    private LocalDateTime createTime;

}

