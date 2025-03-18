package renko.jiang.campus_life_guide.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_life_guide.controller.admin.pojo.dto.NoticeDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.vo.NoticeVO;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.service.NoticeService;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    /**
     * 添加公告
     */

    @PostMapping()
    public Result addNotice(@RequestBody NoticeDTO noticeDTO) {
        return noticeService.addNotice(noticeDTO);
    }

    /**
     * 获取公告列表
     */
    @GetMapping("/page")
    public Result<PageResult<NoticeVO>> pageQueryNotice(NoticeDTO noticeDTO){
        return noticeService.pageQueryNotice(noticeDTO);
    }

    /**
     * 更新公告
     */
    @PutMapping("/{id}")
    public Result updateNotice(@PathVariable Integer id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        return noticeService.updateNotice(noticeDTO);
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public Result deleteNotice(@PathVariable Integer id) {
        return noticeService.deleteNotice(id);
    }
}
