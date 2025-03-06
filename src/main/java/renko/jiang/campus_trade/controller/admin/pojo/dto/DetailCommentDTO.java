package renko.jiang.campus_trade.controller.admin.pojo.dto;


import lombok.Data;
import renko.jiang.campus_trade.pojo.result.PageInfo;

/**
 * @author 86132
 */
@Data
public class DetailCommentDTO extends PageInfo {
    private String detailId;
    private String content;
    private Integer userId;
}
