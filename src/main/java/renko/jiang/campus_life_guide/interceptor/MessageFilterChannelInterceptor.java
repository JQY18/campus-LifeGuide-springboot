package renko.jiang.campus_life_guide.interceptor;

import cn.hutool.json.JSONUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import renko.jiang.campus_life_guide.controller.chat.ChatMessage;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author 86132
 * 对消息内容进行过滤
 */

@Slf4j
@Component
public class MessageFilterChannelInterceptor implements ChannelInterceptor {


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        accessor.setLeaveMutable(true);

        if (Objects.equals(accessor.getCommand(), StompCommand.SEND)) {
            ByteBuffer buffer = ByteBuffer.wrap((byte[]) message.getPayload());
            ChatMessage chatMessage = new ChatMessage();
            try {
                String payload = new String(buffer.array());
                chatMessage = JSONUtil.toBean(payload, ChatMessage.class);
                log.info("消息内容: {}", chatMessage);
            } catch (Exception e) {
                log.error("消息内容解析失败: {}", e.getMessage());
            }

            //默认的替换策略
            String replace = SensitiveWordHelper.replace(chatMessage.getContent());
            //指定替换内容
            //String replace1 = SensitiveWordHelper.replace(text, '*');

            chatMessage.setContent(replace);
            return buildMessage(accessor, JSONUtil.toJsonStr(chatMessage));
        }
        return message;
    }

    private Message<?> buildMessage(StompHeaderAccessor accessor, String msg) {
        byte[] payload = msg.getBytes();
        return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
    }
}
