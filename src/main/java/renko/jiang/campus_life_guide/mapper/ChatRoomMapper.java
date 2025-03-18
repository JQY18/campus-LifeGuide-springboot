package renko.jiang.campus_life_guide.mapper;


import org.apache.ibatis.annotations.Mapper;
import renko.jiang.campus_life_guide.pojo.entity.ChatRoom;

import java.util.List;

/**
 * (ChatRoom)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */

@Mapper
public interface ChatRoomMapper {
    List<ChatRoom> queryByIds(List<Long> chatIds);
}

