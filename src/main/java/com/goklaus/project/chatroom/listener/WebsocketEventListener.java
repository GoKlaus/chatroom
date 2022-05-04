package com.goklaus.project.chatroom.listener;

import java.net.Inet4Address;
import java.net.InetAddress;

import com.goklaus.project.chatroom.model.WsMessage;
import com.goklaus.project.chatroom.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * websocket时间监听器
 */
@Component
@Slf4j
public class WebsocketEventListener {

    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 监听会话建立成功后
     * 
     * @param event 会话建立成功后事件
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        //
        InetAddress inetAddress = null;
        try {
            inetAddress = Inet4Address.getLocalHost();
            log.info("remote client connected this server");
        } catch (Exception e) {
            log.error("handle websockect error {}", e.getMessage());
        }
    }

    /**
     * 监听会话断开
     * 
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User Disconnected : " + username);
            WsMessage chatMessage = new WsMessage();
            chatMessage.setType(WsMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            try {
                redisTemplate.opsForSet().remove(onlineUsers, username);
                redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }

}
