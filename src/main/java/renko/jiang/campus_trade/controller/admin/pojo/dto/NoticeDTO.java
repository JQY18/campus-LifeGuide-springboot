package renko.jiang.campus_trade.controller.admin.pojo.dto;


import lombok.Data;
import renko.jiang.campus_trade.pojo.result.PageInfo;

@Data
public class NoticeDTO extends PageInfo {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private Integer publisher;
}
