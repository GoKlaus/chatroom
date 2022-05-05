package com.goklaus.project.chatroom.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goklaus.project.chatroom.config.properties.RedisProperty;
import com.goklaus.project.chatroom.messageHandler.RedisListenerHandler;
import com.goklaus.project.chatroom.service.ChatService;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置
 */
@Configuration
@EnableCaching
@ConfigurationProperties()
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        template.setValueSerializer(jacksonSeial);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }

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
    RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter,
            RedisProperty redisProperty) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener(adapter, new PatternTopic(redisProperty.getMsgToAll()));
        container.addMessageListener(adapter, new PatternTopic(redisProperty.getUserStatus()));
        return container;
    }

    @Bean
    RedisListenerHandler redisListenerHandler(ChatService chatService, RedisProperty redisProperty,
            RedisConnectionFactory factory) {
        RedisListenerHandler handler = new RedisListenerHandler();
        handler.setChatService(chatService)
                .setRedisProperty(redisProperty)
                .setRedisTemplate(redisTemplate(factory));
        return handler;
    }
}
