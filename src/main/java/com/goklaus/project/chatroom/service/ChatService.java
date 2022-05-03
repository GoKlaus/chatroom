package com.goklaus.project.chatroom.service;

import com.goklaus.project.chatroom.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {

    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendMsg(@Payload Message message) {
        log.info("send msg by simpMessageSendingOperations:{}", message.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", message);
    }

    public void alterUserStatus(@Payload Message message) {
        log.info("alter user online by simpMessageSendingOperations:{}", message.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", message);
    }
    
}
