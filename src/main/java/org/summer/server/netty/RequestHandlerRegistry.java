package org.summer.server.netty;

public interface RequestHandlerRegistry {

    void register(int code, RequestHandler requestHandler);

    RequestHandler find(int code);

}
