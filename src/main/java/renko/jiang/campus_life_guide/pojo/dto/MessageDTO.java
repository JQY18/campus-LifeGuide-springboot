package renko.jiang.campus_life_guide.pojo.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * @author 86132
 */
@Data
public class MessageDTO {
    private Long id;
    private Long chatId;
    private String content;
}
