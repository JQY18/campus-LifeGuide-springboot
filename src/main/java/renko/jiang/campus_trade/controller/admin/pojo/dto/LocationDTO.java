package renko.jiang.campus_trade.controller.admin.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 86132
 */

@Data
public class LocationDTO {
    private Long id;
    private String name;
    private Double[] coords;
    private String description;
    private String detailId;
    private String category;
    private MultipartFile imageFile;
}