package renko.jiang.campus_trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_trade.controller.admin.pojo.dto.NoticeDTO;
import renko.jiang.campus_trade.controller.admin.pojo.entity.Notice;
import renko.jiang.campus_trade.controller.admin.pojo.vo.NoticeVO;
import renko.jiang.campus_trade.mapper.AdminMapper;
import renko.jiang.campus_trade.mapper.NoticeMapper;
import renko.jiang.campus_trade.pojo.result.PageResult;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.service.NoticeService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86132
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 新增通知
     * @param noticeDTO
     * @return
     */
    @Override
    public Result addNotice(NoticeDTO noticeDTO) {
        Notice notice = BeanUtil.copyProperties(noticeDTO, Notice.class);
        int update = noticeMapper.addNotice(notice);
        if (update > 0){
            return Result.success();
        }
        return Result.error("添加失败");
    }


    /***
     * 分页查询
     * @return
     */
    @Override
    public Result<PageResult<NoticeVO>> pageQueryNotice(NoticeDTO noticeDTO) {
        PageResult<NoticeVO> pageResult = new PageResult<>();
        // 查询的起始位置
        int start = (noticeDTO.getPageNo() - 1) * noticeDTO.getPageSize();
        //查询符合条件的总数
        int total = noticeMapper.pageQueryNoticeCount(noticeDTO);
        if (total == 0){
            return Result.success(pageResult);
        }
        //根据条件分页查询公告
        List<Notice> notices = noticeMapper.pageQueryNotice(noticeDTO, start, noticeDTO.getPageSize());

        List<NoticeVO> noticeVOS = new ArrayList<>();
        //查询公告的发布者的username
        for (Notice notice : notices) {
            String username = adminMapper.queryUsernameById(notice.getPublisher());
            NoticeVO noticeVO = BeanUtil.copyProperties(notice, NoticeVO.class, "publisher");
            noticeVO.setPublisher(username);
            noticeVOS.add(noticeVO);
        }

        pageResult.setRecords(noticeVOS);
        // 给分页结果设置总数
        pageResult.setTotal(total);
        return Result.success(pageResult);
    }


    /**
     * 修改公告
     * @param noticeDTO
     * @return
     */
    @Override
    public Result updateNotice(NoticeDTO noticeDTO) {
        Notice notice = BeanUtil.copyProperties(noticeDTO, Notice.class);
        //更新
        if (noticeMapper.updateNotice(BeanUtil.copyProperties(notice, Notice.class)) > 0){
            return Result.success();
        }
        return Result.error("修改失败");
    }

    @Override
    public Result deleteNotice(Integer id) {
        if (noticeMapper.deleteNotice(id) > 0){
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
