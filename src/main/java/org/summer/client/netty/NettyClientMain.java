package org.summer.client.netty;

import org.summer.protocol.JSONRemoteObjectCodec;
import org.summer.protocol.RemoteObject;
import org.summer.protocol.RemoteObjectCodec;
import org.summer.server.ServerConfig;

public class NettyClientMain {

    public static void main(String[] args) {
        ServerConfig config = ServerConfig.loadDefaultProperties();
        RemoteObjectCodec remoteObjectCodec = new JSONRemoteObjectCodec();
        NettyClientBootstrap nettyClientBootstrap = new NettyClientBootstrap(config.getHost(), config.getPort(), remoteObjectCodec);
        nettyClientBootstrap.connect();
        RemoteObject request = new RemoteObject();
    }

}
