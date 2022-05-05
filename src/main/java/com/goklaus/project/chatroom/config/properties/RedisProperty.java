package com.goklaus.project.chatroom.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperty {
    @Value("${server.port:6379}")
    private String serverPort;

    @Value("${redis.channel.msgToAll:websocket.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus:websocket.userStatus}")
    private String userStatus;

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getMsgToAll() {
        return msgToAll;
    }

    public void setMsgToAll(String msgToAll) {
        this.msgToAll = msgToAll;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}
