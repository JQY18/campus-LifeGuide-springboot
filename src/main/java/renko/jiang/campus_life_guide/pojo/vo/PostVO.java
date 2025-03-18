package renko.jiang.campus_life_guide.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostVO {
    private Integer id;
    private Integer userId;
    private String username;
    private String avatar;
    private Integer category;
    private String title;
    private String content;
    private List<String> images;
    private String createTime;
    private Integer likes;
    private Boolean isLiked;
    private Boolean isCollected;
    private Integer comments;
}
