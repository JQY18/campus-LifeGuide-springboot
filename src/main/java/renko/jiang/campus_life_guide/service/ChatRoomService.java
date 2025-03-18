package renko.jiang.campus_life_guide.service;


import renko.jiang.campus_life_guide.controller.chat.ChatMessage;
import renko.jiang.campus_life_guide.pojo.dto.MessageDTO;
import renko.jiang.campus_life_guide.pojo.entity.Message;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.ChatVO;
import renko.jiang.campus_life_guide.pojo.vo.MessageVO;
import renko.jiang.campus_life_guide.pojo.vo.UserInfoVO;

import java.util.List;

/**
 * (ChatRoom)表服务接口
 *
 * @author makejava
 * @since 2025-03-17 22:06:25
 */
public interface ChatRoomService {

    Result<List<MessageVO>> queryMessagesByChatId(Long chatId);

    Result<List<ChatVO>> queryChatRoomsOfCurrentUser();

    Result<Message> sendMessage(MessageDTO messageDTO);

    Result<List<UserInfoVO>> queryChatRoomMembers(Long chatId);

    Result readMessage(Long chatId, Long lastMessageId);

    void saveMessage(ChatMessage chatMessage);
}
