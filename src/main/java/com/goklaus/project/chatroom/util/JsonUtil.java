package com.goklaus.project.chatroom.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 这里使用fastjson实现的工具类
 * todo 使用 gson，jackson， 等等。。。试试看换写法
 */
@Slf4j
public class JsonUtil {

    /**
     * 将java对象转成json字符串
     * 
     * @param source
     * @return
     */
    public static String parseObjToJson(Object source) {
        String result = null;
        try {
            result = JSONObject.toJSONString(source);
        } catch (Exception e) {
            log.error("object to json string error, message {}", e.getMessage());
        }
        return result;
    }

    public static <T> T parseJsonToObj(String source, Class<T> clazz) {
        T result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(source);
            result = jsonObject.toJavaObject(clazz);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("json string to class simple error, message {}", e.getMessage());
        }
        return null;
    }
}
