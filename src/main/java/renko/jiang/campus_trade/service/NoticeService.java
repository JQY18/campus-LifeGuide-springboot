package renko.jiang.campus_trade.service;

import renko.jiang.campus_trade.controller.admin.pojo.dto.NoticeDTO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.NoticeVO;
import renko.jiang.campus_trade.pojo.result.PageResult;
import renko.jiang.campus_trade.pojo.result.Result;

public interface NoticeService {
    Result addNotice(NoticeDTO noticeDTO);

    Result<PageResult<NoticeVO>> pageQueryNotice(NoticeDTO noticeDTO);

    Result updateNotice(NoticeDTO noticeDTO);

    Result deleteNotice(Integer id);
}
