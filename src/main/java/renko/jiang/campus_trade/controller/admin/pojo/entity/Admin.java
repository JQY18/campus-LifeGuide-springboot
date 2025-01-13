package renko.jiang.campus_trade.controller.admin.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private Integer locationId;
    private LocalDateTime createdTime;
}
