package com.ziyuan.websocket;

import com.ziyuan.controller.BaseController;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    private String token = "";

    /**
     * Connection open
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.token = token;
        WebSocketUtil.CLIENTS.put(token, session);
        JsonUtils.objectToJson(session);
        LOG.info("New Connection：token：{}，session id：{}，Connection Count：{}", token, session.getId(), WebSocketUtil.CLIENTS.size());
    }

    /**
     * Close Connection
     */
    @OnClose
    public void onClose(Session session) {
        WebSocketUtil.CLIENTS.remove(this.token);
        LOG.info("Connection Close，token：{}，session id：{}！Connection Count：{}", this.token, session.getId(), WebSocketUtil.CLIENTS.size());
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
}
