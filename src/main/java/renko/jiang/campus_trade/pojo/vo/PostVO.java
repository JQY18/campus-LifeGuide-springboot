package renko.jiang.campus_trade.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostVO {
    private Integer id;
    private Integer userId;
    private Integer category;
    private String title;
    private String content;
    private List<String> images;
    private String createTime;
}
