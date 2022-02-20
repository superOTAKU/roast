package org.summer.domain;


/**
 * 聊天消息
 */
public class ChatMessage {
    public enum ChatMessageTarget {
        TO_USER, TO_CHATROOM
    }

    public enum ChatMessageType {
        TEXT, IMG, FILE
    }

    private Long senderId;
    private ChatMessageTarget target;
    private ChatMessageType type;
    private ChatMessageData data;

}
