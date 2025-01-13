package renko.jiang.campus_trade.controller.admin.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 86132
 */
@Data
public class LocationDetailDTO {
    private String detailId;
    private String name;
    private String description;
    private List<MultipartFile> images;
    private List<MultipartFile> videos;
}
