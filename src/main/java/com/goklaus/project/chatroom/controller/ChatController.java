package com.goklaus.project.chatroom.controller;

import com.goklaus.project.chatroom.model.Message;
import com.goklaus.project.chatroom.service.ChatService;

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


    @Value("${redis.channel.onlineUser")
    private String onlineUser;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @MessageMapping("chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        log.info("send message {}", message);
    }

    @MessageMapping("chat.addUser")
    public void addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("add user , message {}", message);
    }
}
