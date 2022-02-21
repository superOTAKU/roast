package org.summer.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * 将JSON从Map和Object两者间来回转换，优点是简单，缺点是效率很低
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JSONRemoteObjectDataCodec implements RemoteObjectDataCodec {
    private final Class clazz;

    public JSONRemoteObjectDataCodec(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object decode(Map data) {
        return JSON.parseObject(JSON.toJSONString(data), clazz);
    }

    @Override
    public Map<String, Object> encode(Object data) {
        return JSON.parseObject(JSON.toJSONString(data), new TypeReference<>() {});
    }
}
