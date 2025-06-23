package renko.jiang.campus_life_guide.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * (MatchPartner)实体类
 *
 * @author makejava
 * @since 2025-04-07 19:32:34
 */

@Data
public class MatchPartner {
    private Integer id;
    /**
     * 发布者id
     */
    private Integer publisherId;
    /**
     * 兴趣
     */
    private Integer interestId;
    /**
     * 描述
     */
    private String description;
    /**
     * 人数限制
     */
    private Integer limited;
    /**
     * 当前人数
     */
    private Integer currentNum;

    private LocalDateTime createdTime;

}

