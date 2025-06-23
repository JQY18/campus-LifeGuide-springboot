package renko.jiang.campus_life_guide.pojo.dto;

import lombok.Data;


/**
 * @author 86132
 */
@Data
public class MatchPartnerDTO {
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

}
