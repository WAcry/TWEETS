package com.ziyuan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtil {
    public static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketUtil.class);

    public void sendInfo(String token, String message) {
        if (CLIENTS.containsKey(token)) {
            try {
                CLIENTS.get(token).getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("Failed to send message to：{}，message：{}", token, message);
            }
            LOG.info("Successfully send message to：{}，message：{}", token, message);
        }
    }
}
