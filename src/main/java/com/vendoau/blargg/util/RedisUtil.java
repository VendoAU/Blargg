package com.vendoau.blargg.util;

import com.vendoau.blargg.Blargg;
import com.vendoau.blargg.server.ServerInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class RedisUtil {

    private RedisUtil() {}

    public static void publish(String channel, String message) {
        final String ip = Blargg.config().redisAddress().getHostString();
        final int port = Blargg.config().redisAddress().getPort();
        final String password = Blargg.config().redisPassword();

        try (final Jedis jedis = new Jedis(ip, port)) {
            jedis.auth(password);

            jedis.publish(channel, message);
        }
    }

    public static void subscribe(BiConsumer<String, String> callback, String[] channels) {
        final String ip = Blargg.config().redisAddress().getHostString();
        final int port = Blargg.config().redisAddress().getPort();
        final String password = Blargg.config().redisPassword();

        new Thread(() -> {
            try (Jedis jedis = new Jedis(ip, port)) {
                jedis.auth(password);

                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        callback.accept(channel, message);
                    }
                }, channels);
            }
        }).start();
    }

    public static void subscribe(Consumer<String> callback, String channel) {
        subscribe((c, m) -> callback.accept(m), new String[]{channel});
    }

    private static void publishServerInfo(ServerInfo info) {
        final String json = info.toJson().toString();
        publish("info", json);
        publish("info:" + info.name(), json);
    }

    public static void publishServerInfo() {
        publishServerInfo(ServerInfo.get());
    }

    public static void publishOfflineServerInfo() {
        final ServerInfo info = ServerInfo.get();
        info.setPlayerCount(0);
        info.setOnline(false);
        publishServerInfo(info);
    }
}
