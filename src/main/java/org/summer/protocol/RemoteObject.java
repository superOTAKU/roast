package org.summer.protocol;

/**
 * 服务器和客户端交互的对象
 *
 * 如果用于实现聊天服务器，其实大部分情况下根本不需要ACK(等待ACK会很耗时，但某些系统是需要的）
 *
 * @param <V> 消息体类型
 *
 */
public class RemoteObject<V extends RemoteObjectData> {

    public enum RemoteObjectType {
        REQUEST, RESPONSE, SERVER_PUSH
    }

    //请求/响应
    private RemoteObjectType type;

    //请求代码/响应代码
    private int code;

    //客户端分配的消息id，如果是需要响应的请求，客户端必须通过该id匹配响应消息
    private int clientRequestId;

    //请求数据
    private V data;

    public RemoteObject() {}

    public RemoteObject(RemoteObjectType type, int code, int clientRequestId, V data) {
        this.type = type;
        this.code = code;
        this.clientRequestId = clientRequestId;
        this.data = data;
    }

    public RemoteObjectType getType() {
        return type;
    }

    public void setType(RemoteObjectType type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RemoteObjectData getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }

    public int getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(int clientRequestId) {
        this.clientRequestId = clientRequestId;
    }
}
