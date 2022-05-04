package com.goklaus.project.chatroom.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * redis 订阅频道属性类
 */
@Component
@Slf4j
public class RedisListenerBean {
    @Value("${server.port:6379}")
    private String serverPort;

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * 
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener(adapter, new PatternTopic(msgToAll));
        container.addMessageListener(adapter, new PatternTopic(userStatus));
        return container;
    }
}
