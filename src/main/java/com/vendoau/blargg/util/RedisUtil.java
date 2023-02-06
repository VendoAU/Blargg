package com.vendoau.blargg.util;

import com.google.gson.JsonObject;
import com.vendoau.blargg.Blargg;
import net.minestom.server.MinecraftServer;
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

    private static void publishServerInfo(int playerCount, boolean online) {
        final JsonObject info = new JsonObject();
        info.addProperty("playerCount", playerCount);
        info.addProperty("online", online);

        final String name = Blargg.config().serverName();
        publish("info:" + name, info.toString());
    }

    public static void publishServerInfo() {
        publishServerInfo(MinecraftServer.getConnectionManager().getOnlinePlayers().size(), true);
    }

    public static void publishOfflineServerInfo() {
        publishServerInfo(0, false);
    }
}
