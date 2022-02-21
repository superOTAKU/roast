package org.summer.protocol;

import java.util.Map;

/**
 *
 * RemoteObject中Map的编解码，解码：Map -> Object, 编码：Object -> Map
 *
 * @param <T> Map解码后的类型
 */
public interface RemoteObjectDataCodec<T> {

    T decode(Map<String, Object> data);

    Map<String, Object> encode(T data);

}
