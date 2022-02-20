package org.summer.server.netty;

import io.netty.channel.Channel;

/**
 * 客户端会话
 */
public class Session {
    private Channel channel;
    //是否已经登录
    private boolean login = false;
    //该连接代表的用户id
    private Long userId;

    public Session(Channel channel) {
        this.channel = channel;
    }

}
