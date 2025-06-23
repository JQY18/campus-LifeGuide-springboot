package renko.jiang.campus_life_guide.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchPartnerVO {
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
     * 兴趣名称
     */
    private String interestName;
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
    /**
     * 是否已经加入
     */
    private Boolean joined;

    private LocalDateTime createdTime;

}
