package com.goklaus.project.chatroom.redis;

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
 */
@Component
@Slf4j
public class RedisListenerHandler extends MessageListenerAdapter {

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatService chatService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // TODO Auto-generated method stub
        super.onMessage(message, bytes);

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

        if (msgToAll.equals(topic)) {
            log.info("send msg to all users");
            WsMessage wsMessage = JsonUtil.parseJsonToObj(rawMsg, WsMessage.class);
            if (wsMessage != null) {
                chatService.sendMsg(wsMessage);
            }

        } else if (userStatus.equals(topic)) {
            WsMessage wsMessage = JsonUtil.parseJsonToObj(rawMsg, WsMessage.class);
            if (wsMessage != null) {
                chatService.alterUserStatus(wsMessage);
            }
        } else {
            log.warn("No further operation with this topic!");
        }
    }

}
