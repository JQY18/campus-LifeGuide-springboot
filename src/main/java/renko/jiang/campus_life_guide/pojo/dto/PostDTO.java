package renko.jiang.campus_life_guide.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import renko.jiang.campus_life_guide.pojo.result.PageInfo;

import java.util.List;

@Data
public class PostDTO extends PageInfo {
    private Integer userId;
    private String username;
    private Integer category;
    private String title;
    private String content;
    private List<MultipartFile> images;
}
