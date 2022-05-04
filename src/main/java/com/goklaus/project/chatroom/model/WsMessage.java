package com.goklaus.project.chatroom.model;

import java.io.Serializable;

public class WsMessage implements Serializable {

    public static final long serialVersionUID = 1L;

    private MessageType type;

    private String content;

    private String sender;

    /**
     * 消息类型
     */
    public enum MessageType {
        CHAT, // 聊天类型
        JOIN, // 上线
        LEAVE // 下线
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message [content=" + content + ", sender=" + sender + ", type=" + type + "]";
    }

}
