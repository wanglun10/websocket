package com.wfs.websocket.simple;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wfs.websocket.MessageDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：websocket服务
 * 每个连接都会创建一个WebSocketServer实例
 *
 * @author wfs
 * @since 2020/3/25
 */
@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    private static final ConcurrentHashMap<String, WebSocketServer> serverMap = new ConcurrentHashMap();

    private Session session;

    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.info("连接成功：userId:{}", userId);
        this.session = session;
        this.userId = userId;
        serverMap.put(userId, this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (!StringUtils.isEmpty(message)) {
            MessageDto msgObject = null;
            try {
                msgObject = JSON.parseObject(message, MessageDto.class);
            } catch (Exception e) {
                log.error("解析json失败，message：{}", message, e);
                throw new IllegalArgumentException("参数不符合规范");
            }
            // 放入发送人
            msgObject.setFromUserId(userId);
            WebSocketServer.sendMsg(msgObject);
        }

    }

    private void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }

    @OnClose
    public void onClose() {
        log.info("断开连接，userId：{}", userId);
        serverMap.remove(userId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生异常，用户：{}", userId, throwable);
    }

    public static void sendMsg(MessageDto message) {
        if (StringUtils.isBlank(message.getToUserId())) {
            log.error("userId为空，message：{}", message);
        }
        WebSocketServer socketServer = serverMap.get(message.getToUserId());
        if (socketServer != null) {
            socketServer.sendMessage(message.getMessage());
        } else {
            log.info("发送的用户不在线:{}", message);
        }
    }

}
