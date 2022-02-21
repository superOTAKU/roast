package org.summer.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.summer.protocol.RemoteObject;

public class ResponseHandler extends SimpleChannelInboundHandler<RemoteObject> {
    private final ResponseFutureHolder responseFutureHolder;

    public ResponseHandler(ResponseFutureHolder responseFutureHolder) {
        this.responseFutureHolder = responseFutureHolder;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteObject remoteObject) throws Exception {
        if (remoteObject.getType() == RemoteObject.RemoteObjectType.RESPONSE) {
            this.responseFutureHolder.completeFuture(remoteObject);
        }
        //TODO Server Push类型消息的处理...可以考虑投递到一个阻塞队列，异步拉取任务进行处理
        //或者直接在这里分发逻辑处理
    }
}
