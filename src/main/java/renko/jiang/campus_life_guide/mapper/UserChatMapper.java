package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.entity.UserChat;

import java.util.List;
import java.util.Map;

/**
 * (UserChat)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-17 22:25:53
 */
@Mapper
public interface UserChatMapper {

    @Select("select * from user_chat where user_id = #{userId}")
    List<UserChat> queryChatRoomsOfCurrentUser(Integer userId);

    @Update("update user_chat set last_read = #{messageId} where user_id = #{userId} and chat_id = #{chatId}")
    Integer updateLastRead(Long messageId, Integer userId, Long chatId);

    List<Map<String, Object>> queryPrivateChatUser(List<UserChat> privateChatUsers, Integer userId);

    List<Map<String, Object>> querySenders(List<Message> messages);

    @Select("select user_id,role from user_chat where chat_id = #{chatId}")
    List<UserChat> queryChatRoomMembers(Long chatId);

    Long queryChatIdBetweenFriends(Integer userId, Integer friendId);

    @Insert("insert into user_chat(user_id, chat_id,type) values(#{userId}, #{chatId},'private'),(#{friendId},#{chatId},'private')")
    int addFriend(Integer userId, Integer friendId, Long chatId);

    @Delete("delete from user_chat where chat_id = #{chatId}")
    long deleteByChatId(Long chatId);

    long addGroupChatRoom(Integer ownerId, Long chatId, List<Integer> userIds);

    @Select("select user_id from user_chat where chat_id = #{chatId} and role = 'owner'")
    Integer queryChatRoomOwner(Long chatId);

    @Delete("delete from user_chat where user_id = #{userId} and chat_id = #{chatId}")
    int exitGroupChat(Integer userId, Long chatId);
}

