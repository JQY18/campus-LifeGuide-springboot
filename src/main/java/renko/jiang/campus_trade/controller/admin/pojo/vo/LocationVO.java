package renko.jiang.campus_trade.controller.admin.pojo.vo;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class LocationVO {
    private Long id;
    private String name;
    private Double[] coords;
    private String description;
    private String image;
    private String detailId;
    private String category;
    private LocalDateTime createdTime;
}