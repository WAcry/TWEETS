package com.ziyuan.websocket;

import com.ziyuan.controller.BaseController;
import com.ziyuan.pojo.bo.UserBO;
import com.ziyuan.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/websocket")
public class WebSocketServer extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
    private static HashMap<String, Session> map = new HashMap<>();
    /**
     * token is user's auth token get when login
     */
    private String token = "";

    /**
     * Connection open
     */
    @OnOpen
    public void onOpen(Session session, @RequestBody UserBO userBO) {
        if (!auth(userBO.getUsername(), userBO.getToken())) return;
        token = userBO.getToken();
        map.put(token, session);
        JsonUtils.objectToJson(session);
        LOG.info("New Connection：token：{}，session id：{}，Connection Count：{}", token, session.getId(), map.size());
    }

    /**
     * Close Connection
     */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("Connection Close，token：{}，session id：{}！Connection Count：{}", this.token, session.getId(), map.size());
    }

    /**
     * Receive Message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("Receive：{}，Message：{}", token, message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("Error", error);
    }

    // TODO: make it distributed by MQ
    public void sendInfoToAll(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("Failed to send message to：{}，message：{}", token, message);
            }
            LOG.info("Successfully send message to：{}，message：{}", token, message);
        }
    }

    public void sendInfoToOne(String token, String message) {
        Session session = map.get(token);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOG.error("Failed to send message to：{}，message：{}", token, message);
        }
        LOG.info("Successfully send message to：{}，message：{}", token, message);
    }
}
