package org.summer.server.netty;

import io.netty.channel.ChannelHandlerContext;
import org.summer.domain.ChatManager;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.ResponseCode;
import org.summer.protocol.data.ErrorData;

import java.util.Map;

/**
 * 封装一个请求，提供回消息等便捷的方法
 */
public class Request {
    private final ChannelHandlerContext ctx;
    private final RemoteObject remoteObject;
    private final ChatManager chatManager;

    public Request(ChannelHandlerContext ctx, RemoteObject remoteObject, ChatManager chatManager) {
        this.ctx = ctx;
        this.remoteObject = remoteObject;
        this.chatManager = chatManager;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public RemoteObject getRemoteObject() {
        return remoteObject;
    }

    public void replyError(ErrorData errorData) {
        RemoteObject remoteObject = new RemoteObject();
        remoteObject.setClientRequestId(remoteObject.getClientRequestId());
        remoteObject.setCode(ResponseCode.ERROR);
        remoteObject.setType(RemoteObject.RemoteObjectType.RESPONSE);
        remoteObject.setData(errorData.toDataMap());
        ctx.channel().writeAndFlush(remoteObject);
    }

    public void replyOk(Map<String, Object> data) {
        RemoteObject remoteObject = new RemoteObject();
        remoteObject.setClientRequestId(remoteObject.getClientRequestId());
        remoteObject.setType(RemoteObject.RemoteObjectType.RESPONSE);
        remoteObject.setCode(ResponseCode.OK);
        remoteObject.setData(data);
        ctx.channel().writeAndFlush(remoteObject);
    }

}
