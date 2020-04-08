package com.wfs.websocket.minor;

import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：拦截器
 *
 * @author wfs
 * @since 2020/3/25
 */
@Slf4j
public class ConnectInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest httpServletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest servletRequest = httpServletRequest.getServletRequest();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            attributes.put(paramName, servletRequest.getParameter(paramName));
        }
        boolean res = !StringUtils.isBlank(Optional.ofNullable(attributes.get("userId")).orElse("").toString());
        if (!res) {
            log.error("userId是必传参数");
        }
        return res;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
