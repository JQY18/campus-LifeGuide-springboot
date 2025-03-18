package renko.jiang.campus_life_guide.service;

import renko.jiang.campus_life_guide.controller.admin.pojo.dto.NoticeDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.vo.NoticeVO;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;

public interface NoticeService {
    Result addNotice(NoticeDTO noticeDTO);

    Result<PageResult<NoticeVO>> pageQueryNotice(NoticeDTO noticeDTO);

    Result updateNotice(NoticeDTO noticeDTO);

    Result deleteNotice(Integer id);
}
