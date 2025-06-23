package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (Interest)实体类
 *
 * @author makejava
 * @since 2025-04-07 19:32:32
 */
@Data
public class Interest {

    private Integer id;

    private String name;

    private LocalDateTime createTime;
}

