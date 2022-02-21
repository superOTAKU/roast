package org.summer.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.RemoteObjectCodec;
import org.summer.server.netty.MessageCodec;
import org.summer.server.netty.NettyServerBootstrap;
import org.summer.server.netty.RequestHandlerRegistry;
import org.summer.server.netty.SessionManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class NettyClientBootstrap {
    private final String host;
    private final int port;
    private final RemoteObjectCodec remoteObjectCodec;
    private final AtomicReference<ClientState> state = new AtomicReference<>(ClientState.INIT);
    private Channel channel;
    private NioEventLoopGroup group;
    private final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    private ResponseFutureHolder responseFutureHolder = new ResponseFutureHolder();

    public enum ClientState {
        INIT, CONNECTING, CONNECT_FAIL, CONNECTED, RECONNECTING, STOPPING, STOPPED
    }

    public NettyClientBootstrap(String host, int port, RemoteObjectCodec remoteObjectCodec) {
        this.host = host;
        this.port = port;
        this.remoteObjectCodec = remoteObjectCodec;
    }

    public void connect() {
        if (!state.compareAndSet(ClientState.INIT, ClientState.CONNECTING)) {
            throw new IllegalStateException();
        }
        group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        channel = bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //定长消息解码
                        ch.pipeline().addFirst(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        //消息编解码
                        ch.pipeline().addLast(new MessageCodec(remoteObjectCodec));
                        //业务逻辑处理
                        ch.pipeline().addLast(new ResponseHandler(responseFutureHolder));
                    }
                })
                .connect(host, port)
                .syncUninterruptibly()
                .channel();
        log.info("connected to server {}:{}", host, port);
        state.set(ClientState.CONNECTED);
    }

    //只发消息，不等响应
    public void sendRequestAsync(RemoteObject request) {
        request.setClientRequestId(requestIdGenerator.incrementAndGet());
        channel.writeAndFlush(request);
    }

    //发送消息并同步等待响应
    public RemoteObject sendForResponse(RemoteObject request) {
        request.setClientRequestId(requestIdGenerator.incrementAndGet());
        CompletableFuture<RemoteObject> responseFuture = responseFutureHolder.addFuture(request.getClientRequestId());
        channel.writeAndFlush(request);
        try {
            return responseFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
