package org.summer.domain;

import cn.hutool.core.lang.Snowflake;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天系统
 *
 * 可以把领域对象抽象出来，但是如何抽象出网络层？
 *
 */
public class ChatManager {
    //当前在线用户
    private final Map<Long, User> remoteUsers = new ConcurrentHashMap<>();
    //当前创建的聊天室
    private final Map<Long, ChatRoom> chatRooms = new ConcurrentHashMap<>();
    //当前已占用的nickName
    private final Set<String> nickNames = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Snowflake userIdGenerator = new Snowflake();


    public User createUser(String nickName) {
        if (!nickNames.add(nickName)) {
            return null;
        }
        User user = new User(userIdGenerator.nextId(), nickName);
        remoteUsers.put(user.getId(), user);
        return user;
    }

}
