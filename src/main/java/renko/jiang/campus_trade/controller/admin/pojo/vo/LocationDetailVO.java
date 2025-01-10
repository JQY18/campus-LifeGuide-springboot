package renko.jiang.campus_trade.controller.admin.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 86132
 */
@Data
public class LocationDetailVO {
    private String name;
    private String description;
    private List<String> images;
    private List<String> videos;
}
