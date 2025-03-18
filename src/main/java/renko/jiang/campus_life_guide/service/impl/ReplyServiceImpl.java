package renko.jiang.campus_life_guide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_life_guide.mapper.ReplyMapper;
import renko.jiang.campus_life_guide.pojo.dto.ReplyDTO;
import renko.jiang.campus_life_guide.service.ReplyService;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyMapper replyMapper;
    @Override
    public void addReply(ReplyDTO replyDTO) {
        replyMapper.addReply(replyDTO);
    }
}
