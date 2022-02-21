package org.summer.client.netty;

import org.summer.protocol.RemoteObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseFutureHolder {
    private final Map<Integer, CompletableFuture<RemoteObject>> responseFutures = new ConcurrentHashMap<>();

    public CompletableFuture<RemoteObject> addFuture(Integer requestId) {
        CompletableFuture<RemoteObject> responseFuture  = new CompletableFuture<>();
        responseFutures.put(requestId, responseFuture);
        return responseFuture;
    }

    public void completeFuture(RemoteObject response) {
        if (response.getType() != RemoteObject.RemoteObjectType.RESPONSE) {
            return;
        }
        CompletableFuture<RemoteObject> future = responseFutures.remove(response.getClientRequestId());
        if (future != null) {
            future.complete(response);
        }
    }

}
