package org.summer.protocol;

/**
 * 将消息编码为JSON，记得在消息前加长度字段，实际开发可能使用protobuf等序列号协议（自己开发比较麻烦，当然也可以自己设计，比如几个字节字段名，几个字节字段类型，后续字节字段内容）
 */
public class JSONRemoteObjectCodec implements RemoteObjectCodec {

    @Override
    public byte[] encode(RemoteObject remoteObject) {
        return new byte[0];
    }

    @Override
    public RemoteObject decode(byte[] bytes) {
        return null;
    }
}
