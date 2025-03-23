package renko.jiang.campus_life_guide.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author 86132
 */
@Data
public class GroupChatDTO {
    private Long id;
    @Schema(description = "群聊名称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    private String avatar;
    @Schema(description = "群聊成员id",requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> userIds;
}
