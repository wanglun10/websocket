package com.wfs.websocket.minor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 功能描述：配置类
 *
 * @author wfs
 * @since 2020/3/25
 */
@Configuration
@EnableWebSocket
@EnableScheduling
public class SocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketMsgHandler(), "/ws-server").setAllowedOrigins("*").addInterceptors(connectInterceptor());
    }

    /**
     * 消息处理类
     * @return
     */
    @Bean
    public WebSocketMsgHandler webSocketMsgHandler() {
        return new WebSocketMsgHandler();
    }

    /**
     * 握手时的拦截器
     * @return
     */
    @Bean
    public ConnectInterceptor connectInterceptor() {
        return new ConnectInterceptor();
    }
}
