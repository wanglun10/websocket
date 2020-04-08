package com.wfs.websocket.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：
 *
 * @author wfs
 * @since 2020/3/26
 */
@RestController
@Slf4j
public class MessageController {
    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    public String sendAll(String message) {
        log.info("全量消息收到：{}", message);
        return message;
    }

    @SubscribeMapping("/all")
    public String subAll() {
        return "dasda";
    }
}
