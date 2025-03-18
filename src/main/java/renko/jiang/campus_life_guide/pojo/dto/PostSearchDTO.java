package renko.jiang.campus_life_guide.pojo.dto;

import lombok.Data;

@Data
public class PostSearchDTO {
    private Integer userId;
    private String category;
    private String title;
    private String content;
}
