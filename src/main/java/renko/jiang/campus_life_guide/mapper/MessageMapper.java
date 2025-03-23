package renko.jiang.campus_life_guide.mapper;


import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.entity.UserChat;

import java.util.List;
import java.util.Map;

/**
 * (Message)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */
@Mapper
public interface MessageMapper {
    List<Map<String, Object>> queryUnreadCount(List<UserChat> userChats);

    List<Message> queryLastMessage(List<Long> chatIds);

    @Select("select * from message where chat_id = #{chatId} order by id")
    List<Message> queryMessagesByChatId(Long chatId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into message (chat_id, sender_id, content) values (#{chatId}, #{senderId}, #{content})")
    Integer insert(Message message);

    @Delete("delete from message where chat_id = #{chatId}")
    long deleteByChatId(Long chatId);
}

