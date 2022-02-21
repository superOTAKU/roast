package org.summer.server;

import lombok.Data;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

@Data
public class ServerConfig {
    private String host;
    private int port;

    public static ServerConfig loadDefaultProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(Objects.requireNonNull(ServerConfig.class.getResourceAsStream("/config.properties"))));
            ServerConfig config = new ServerConfig();
            config.setHost(properties.getProperty("server.host", "0.0.0.0"));
            config.setPort(Integer.parseInt(properties.getProperty("server.port", "80")));
            return  config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
