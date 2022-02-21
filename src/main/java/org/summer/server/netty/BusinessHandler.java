package org.summer.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.summer.domain.ChatManager;
import org.summer.protocol.ErrorCode;
import org.summer.protocol.JSONRemoteObjectDataCodec;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.ResponseCode;
import org.summer.protocol.data.ErrorData;

import java.util.concurrent.ExecutorService;

public class BusinessHandler extends SimpleChannelInboundHandler<RemoteObject> {
    private final RequestHandlerRegistry requestHandlerRegistry;
    private final ExecutorService service;
    private final ChatManager chatManager;

    public BusinessHandler(ExecutorService service, RequestHandlerRegistry requestHandlerRegistry, ChatManager chatManager) {
        this.service = service;
        this.requestHandlerRegistry = requestHandlerRegistry;
        this.chatManager = chatManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        RequestHandler requestHandler = requestHandlerRegistry.find(remoteObject.getCode());
        if (requestHandler == null) {
            ErrorData errorData = new ErrorData(ErrorCode.REQUEST_CODE_N0T_FOUND, null, null);
            JSONRemoteObjectDataCodec dataCodec = new JSONRemoteObjectDataCodec(ErrorData.class);
            RemoteObject responseObj = new RemoteObject(RemoteObject.RemoteObjectType.RESPONSE, ResponseCode.ERROR,
                    remoteObject.getClientRequestId(), dataCodec.encode(errorData));
            ctx.writeAndFlush(responseObj);
            return;
        }
        //投递任务到业务线程池处理
        service.execute(() -> requestHandler.handle(new Request(ctx, remoteObject, chatManager)));
    }

}
