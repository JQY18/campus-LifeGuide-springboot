package renko.jiang.campus_life_guide.mapper;


import org.apache.ibatis.annotations.*;
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

    @Delete("delete from chat_room where id = #{chatId}")
    long deleteChatRoomByChatId(Long chatId);

    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    long addChatRoom(ChatRoom chatRoom);

    @Select("select count(1) from user_chat where chat_id = #{chatId}")
    int existChatRoom(Long chatId);
}

