package org.summer.server.netty;

import io.netty.channel.ChannelHandlerContext;
import org.summer.protocol.RemoteObject;

/**
 * 封装一个请求，提供回消息等便捷的方法
 */
public class Request {
    private ChannelHandlerContext ctx;
    private RemoteObject remoteObject;

    public Request(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        this.ctx = ctx;
        this.remoteObject = remoteObject;
    }

}
