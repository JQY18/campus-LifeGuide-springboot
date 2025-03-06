package renko.jiang.campus_trade.controller.admin.pojo.vo;


import lombok.Data;


@Data
public class NoticeVO {
    private Integer id;
    private String title;
    private String content;
    private String type;
    // 发布者的用户名username
    private String publisher;
    private String createTime;
}
