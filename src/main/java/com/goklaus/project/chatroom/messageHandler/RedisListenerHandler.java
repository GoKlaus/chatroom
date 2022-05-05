package com.goklaus.project.chatroom.messageHandler;

import com.goklaus.project.chatroom.config.properties.RedisProperty;
import com.goklaus.project.chatroom.model.WsMessage;
import com.goklaus.project.chatroom.service.ChatService;
import com.goklaus.project.chatroom.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * redis 订阅频道处理类
 * 消息监听器适配器，绑定消息处理器
 */
@Slf4j
public class RedisListenerHandler extends MessageListenerAdapter {

    private RedisProperty redisProperty;

    private RedisTemplate<String, Object> redisTemplate;

    private ChatService chatService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String rawMsg;
        String topic;

        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            log.info("received msg from topic: {}, row message content {}", rawMsg, topic);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            return;
        }

        if (redisProperty.getMsgToAll().equals(topic)) {
            log.info("send msg to all users");
            WsMessage wsMessage = JsonUtil.parseJsonToObj(rawMsg, WsMessage.class);
            if (wsMessage != null) {
                chatService.sendMsg(wsMessage);
            }

        } else if (redisProperty.getUserStatus().equals(topic)) {
            WsMessage wsMessage = JsonUtil.parseJsonToObj(rawMsg, WsMessage.class);
            if (wsMessage != null) {
                chatService.alterUserStatus(wsMessage);
            }
        } else {
            log.warn("No further operation with this topic!");
        }
    }

    public RedisProperty getRedisProperty() {
        return redisProperty;
    }

    public RedisListenerHandler setRedisProperty(RedisProperty redisProperty) {
        this.redisProperty = redisProperty;
        return this;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisListenerHandler setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public RedisListenerHandler setChatService(ChatService chatService) {
        this.chatService = chatService;
        return this;
    }

}
