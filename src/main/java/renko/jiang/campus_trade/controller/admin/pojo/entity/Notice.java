package renko.jiang.campus_trade.controller.admin.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class Notice {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private Integer publisher;
    private LocalDateTime createTime;
}
