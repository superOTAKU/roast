package org.summer.server.netty;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerRegistryImpl implements RequestHandlerRegistry {
    private Map<Integer, RequestHandler> requestHandlerMap = new HashMap<>();

    @Override
    public void register(int code, RequestHandler requestHandler) {
        requestHandlerMap.put(code, requestHandler);
    }

    @Override
    public RequestHandler find(int code) {
        return requestHandlerMap.get(code);
    }

}
