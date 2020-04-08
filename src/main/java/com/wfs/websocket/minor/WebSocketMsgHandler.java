package com.wfs.websocket.minor;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.wfs.websocket.MessageDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：消息处理
 *
 * @author wfs
 * @since 2020/3/25
 */
@Slf4j
public class WebSocketMsgHandler extends AbstractWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        log.info("连接成功:{}", attributes);
        SESSION_CONCURRENT_HASH_MAP.put((String) attributes.get("userId"), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到消息：{}", message.getPayload());
        try {
            MessageDto messageDto = JSON.parseObject(message.getPayload(), MessageDto.class);
            messageDto.setFromUserId((String) session.getAttributes().get("userId"));
            sendMsg(messageDto);
        } catch (JSONException e) {
            log.error("处理消息异常,message:{}", message.getPayload(), e);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("收到异常");
        if (exception != null) {
            log.error("消息处理异常", exception);
            session.sendMessage(new TextMessage("dasdads"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        log.info("断开连接：{}", JSON.toJSONString(attributes));
        SESSION_CONCURRENT_HASH_MAP.remove(attributes.get("userId"), session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    private static void sendMsg(MessageDto messageDto) throws IOException {
        if (StringUtils.isBlank(messageDto.getToUserId())) {
            log.error("发送人为空:{}", messageDto);
        }
        WebSocketSession webSocketSession = SESSION_CONCURRENT_HASH_MAP.get(messageDto.getToUserId());
        if (Objects.nonNull(webSocketSession)) {
            webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(messageDto)));
        } else {
            log.info("发送的用户不在线:{}", messageDto);
        }
    }
}
