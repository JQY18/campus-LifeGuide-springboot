package renko.jiang.campus_trade.pojo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86132
 */
@Data
public class PageInfo {
    private Integer pageNo = 1;

    private Integer pageSize = 20;

    private String startTime;

    private String endTime;

    public Integer getPageNo() {
        if (pageNo == null || pageNo < 1) {
            return 1;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1 || pageSize > Integer.MAX_VALUE) {
            return 20;
        }
        return pageSize;
    }

}
