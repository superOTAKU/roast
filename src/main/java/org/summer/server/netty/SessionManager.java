package org.summer.server.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端会话管理
 */
@Slf4j
public class SessionManager {
    //channelId -> session
    private final Map<ChannelId, Session> clientChannels = new ConcurrentHashMap<>();
    //userId -> session
    private final Map<Long, Session> userSessions = new ConcurrentHashMap<>();

    /**
     * 假设是需要登录的，那么登录之前是只能拿到连接的信息，在这里只需要根据ChannelId初始化对应的Session对象即可
     */
    public void channelActive(Channel newChannel) {
        Session session = new Session(newChannel);
        //为Channel绑定Session
        AttributeKey<Session> sessionAttr  = AttributeKey.valueOf("session");
        newChannel.attr(sessionAttr).set(session);
        clientChannels.put(newChannel.id(), session);
        log.info("new channel active, channelId: {}", newChannel.id());
    }

    public void channelInactive(Channel inactiveChannel) {
        inactiveChannel.attr(AttributeKey.<Session>valueOf("session")).set(null);
        clientChannels.remove(inactiveChannel.id());
        log.info("channel {} inactive", inactiveChannel.id());
        //TODO 是否需要一些清理工作？
    }

}
