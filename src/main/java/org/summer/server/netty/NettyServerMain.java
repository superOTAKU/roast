package org.summer.server.netty;

import org.summer.protocol.JSONRemoteObjectCodec;
import org.summer.protocol.RemoteObjectCodec;
import org.summer.protocol.RequestCode;
import org.summer.protocol.requesthandler.LoginRequestHandler;
import org.summer.server.ServerConfig;

public class NettyServerMain {

    public static void main(String[] args) {
        //读取配置
        ServerConfig config = ServerConfig.loadDefaultProperties();
        //初始化NettyBootstrap
        RemoteObjectCodec remoteObjectCodec = new JSONRemoteObjectCodec();
        RequestHandlerRegistry requestHandlerRegistry = new RequestHandlerRegistryImpl();
        //注册请求处理器
        requestHandlerRegistry.register(RequestCode.LOGIN, new LoginRequestHandler());
        SessionManager sessionManager = new SessionManager();
        NettyServerBootstrap nettyServerBootstrap = new NettyServerBootstrap(config.getHost(), config.getPort(),
                remoteObjectCodec, requestHandlerRegistry, sessionManager);
        nettyServerBootstrap.start();
    }

}
