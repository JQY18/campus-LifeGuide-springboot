package renko.jiang.campus_life_guide.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_life_guide.pojo.dto.ReplyDTO;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.service.ReplyService;

/**
 * @author 86132
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @PostMapping("/add")
    public Result addReply(@RequestBody ReplyDTO replyDTO) {
        replyService.addReply(replyDTO);
        return Result.success();
    }
}
