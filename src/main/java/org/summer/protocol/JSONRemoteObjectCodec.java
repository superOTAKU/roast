package org.summer.protocol;

import com.alibaba.fastjson.JSON;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 将消息编码为JSON，记得在消息前加长度字段，实际开发可能使用protobuf等序列号协议（自己开发比较麻烦，当然也可以自己设计，比如几个字节字段名，几个字节字段类型，后续字节字段内容）
 */
public class JSONRemoteObjectCodec implements RemoteObjectCodec {

    @Override
    public byte[] encode(RemoteObject remoteObject) {
        byte[] data = JSON.toJSONString(remoteObject).getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + 4);
        byteBuffer.putInt(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }

    @Override
    public RemoteObject decode(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        int length = byteBuffer.getInt();
        byte[] data = new byte[length];
        byteBuffer.get(data);
        return JSON.parseObject(new String(data, StandardCharsets.UTF_8), RemoteObject.class);
    }
}
