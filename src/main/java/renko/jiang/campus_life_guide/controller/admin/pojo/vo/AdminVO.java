package renko.jiang.campus_life_guide.controller.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class AdminVO {
    private Integer id;
    private String username;
    private Integer locationId;
    private LocalDateTime createdTime;
    //可管理地点名称
    private String locationName;
}
