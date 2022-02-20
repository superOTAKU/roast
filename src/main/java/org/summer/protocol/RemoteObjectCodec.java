package org.summer.protocol;

public interface RemoteObjectCodec {

    byte[] encode(RemoteObject remoteObject);

    RemoteObject decode(byte[] bytes);

}
