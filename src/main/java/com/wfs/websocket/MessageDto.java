package com.wfs.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能描述：
 *
 * @author wfs
 * @since 2020/3/25
 */
@Getter
@Setter
@ToString
public class MessageDto {
    private String fromUserId;

    private String toUserId;

    private String message;
}
