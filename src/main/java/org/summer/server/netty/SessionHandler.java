package org.summer.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * session管理，通过channelActive和channelInactive，委托给对应的管理器进行管理
 */
public class SessionHandler extends ChannelInboundHandlerAdapter {
    private final SessionManager sessionManager;

    public SessionHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.channelActive(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.channelInactive(ctx.channel());
        super.channelInactive(ctx);
    }
}
