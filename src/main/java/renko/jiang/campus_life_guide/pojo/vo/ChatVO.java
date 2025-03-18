package renko.jiang.campus_life_guide.pojo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import renko.jiang.campus_life_guide.pojo.entity.Message;


/**
 * @author 86132
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ChatVO {
    private Long id;
    // 私聊对应的用户id
    private Integer userId;
    private String type;
    private String name;
    private String avatar;
    private Boolean online;
    private Integer unreadCount;
    private Message lastMessage;
}
