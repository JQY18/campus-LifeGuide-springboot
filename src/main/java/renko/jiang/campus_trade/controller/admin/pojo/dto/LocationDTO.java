package renko.jiang.campus_trade.controller.admin.pojo.dto;

import lombok.Data;

/**
 * @author 86132
 */

@Data
public class LocationDTO {
    private String name;
    private Double[] coords;
    private String description;
    private String image;
    private String detailId;
    private String category;
}