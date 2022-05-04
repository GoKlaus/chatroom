package com.goklaus.project.chatroom.controller;

import com.goklaus.project.chatroom.model.WsMessage;
import com.goklaus.project.chatroom.service.ChatService;
import com.goklaus.project.chatroom.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

    /**
     * 群发信息
     */
    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.onlineUsers")
    private String onlineUsers;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @MessageMapping("chat.sendMessage")
    public void sendMessage(@Payload WsMessage message) {
        log.info("send message {}", message);
        try {
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(message));
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
        }
    }

    @MessageMapping("chat.addUser")
    public void addUser(@Payload WsMessage message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("add user , message {}", message);
        try {
            headerAccessor.getSessionAttributes().put("username", message.getSender());
            redisTemplate.opsForSet().add(onlineUsers, message.getSender());
            redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(message));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
