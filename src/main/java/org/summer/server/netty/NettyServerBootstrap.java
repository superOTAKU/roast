package org.summer.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.summer.domain.ChatManager;
import org.summer.protocol.RemoteObjectCodec;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class NettyServerBootstrap {
    private final String host;
    private final int port;
    private final RemoteObjectCodec remoteObjectCodec;
    private final RequestHandlerRegistry requestHandlerRegistry;
    private final SessionManager sessionManager;
    private final AtomicReference<ServerState> state = new AtomicReference<>(ServerState.INIT);
    private Channel serverChannel;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ChatManager chatManager = new ChatManager();
    private final ExecutorService businessService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public enum ServerState {
        INIT, STARTING, START_FAIL, RUNNING, STOPPING, STOPPED
    }

    public NettyServerBootstrap(String host, int port, RemoteObjectCodec remoteObjectCodec, RequestHandlerRegistry requestHandlerRegistry, SessionManager sessionManager) {
        this.host = host;
        this.port = port;
        this.remoteObjectCodec = remoteObjectCodec;
        this.requestHandlerRegistry = requestHandlerRegistry;
        this.sessionManager = sessionManager;
    }

    public void start() {
        if (!state.compareAndSet(ServerState.INIT, ServerState.STARTING)) {
            throw new IllegalStateException();
        }
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(4);
        try {
            serverChannel = bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //定长消息解码
                            ch.pipeline().addFirst(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                            //消息编解码
                            ch.pipeline().addLast(new MessageCodec(remoteObjectCodec));
                            //连接管理
                            ch.pipeline().addLast(new SessionHandler(sessionManager));
                            //业务逻辑处理
                            ch.pipeline().addLast(new BusinessHandler(businessService, requestHandlerRegistry, chatManager));
                        }
                    })
                    .bind(host, port)
                    .syncUninterruptibly().channel();
            log.info("server started at {}:{}", host, port);
            state.set(ServerState.RUNNING);
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            state.set(ServerState.START_FAIL);
        }
    }

    public void shutdown() {
        if (!state.compareAndSet(ServerState.RUNNING, ServerState.STOPPED)) {
            throw new IllegalStateException();
        }
        serverChannel.close().syncUninterruptibly();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
