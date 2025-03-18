package renko.jiang.campus_life_guide.service.impl;

import jakarta.annotation.Resource;
import renko.jiang.campus_life_guide.mapper.MessageMapper;
import renko.jiang.campus_life_guide.service.MessageService;
import org.springframework.stereotype.Service;


/**
 * (Message)表服务实现类
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;

}
