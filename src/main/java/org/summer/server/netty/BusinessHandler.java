package org.summer.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.summer.protocol.ErrorCode;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.ResponseCode;
import org.summer.protocol.data.ErrorData;

public class BusinessHandler extends SimpleChannelInboundHandler<RemoteObject> {
    private final RequestHandlerRegistry requestHandlerRegistry;

    public BusinessHandler(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        RequestHandler requestHandler = requestHandlerRegistry.find(remoteObject.getCode());
        if (requestHandler == null) {
            ErrorData errorData = new ErrorData(ErrorCode.REQUEST_CODE_N0T_FOUND, null, null);
            RemoteObject responseObj = new RemoteObject(RemoteObject.RemoteObjectType.RESPONSE, ResponseCode.ERROR,
                    remoteObject.getClientRequestId(), errorData);
            ctx.writeAndFlush(responseObj);
            return;
        }
        requestHandler.handle(new Request(ctx, remoteObject));
    }

}
