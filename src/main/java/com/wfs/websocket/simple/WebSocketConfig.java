package com.wfs.websocket.simple;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 功能描述：开启websocket支持
 *
 * @author wfs
 * @since 2020/3/25
 */
@Configuration
public class WebSocketConfig {
    /**
     * Simple WebSocket 基于注解实现
     * ServerEndpointExporter 是由Spring官方提供的标准实现，用于扫描ServerEndpointConfig配置类和@ServerEndpoint注解实例
     * 如果使用默认的嵌入式容器 比如Tomcat 则必须手工在上下文提供ServerEndpointExporter
     * 如果使用外部容器部署war包，则不需要提供提供ServerEndpointExporter，因为此时SpringBoot默认将扫描服务端的行为交给外部容器处理，所以线上部署的时候要把WebSocketConfig中这段注入bean的代码注掉
     *
     * @return
     */
    //    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
