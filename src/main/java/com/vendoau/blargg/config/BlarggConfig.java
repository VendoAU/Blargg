package com.vendoau.blargg.config;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class BlarggConfig extends ConfigManager {

    private String serverName;
    private InetSocketAddress serverAddress;

    private String velocitySecret;

    private InetSocketAddress redisAddress;
    private String redisPassword;

    private Pos spawn;
    private GameMode gamemode;

    public BlarggConfig() {
        super(new File("blargg.conf"), "/blargg.conf");
    }

    @Override
    public void load() throws IOException {
        super.load();

        final CommentedConfigurationNode server = config().node("server");
        serverName = server.node("name").getString();
        serverAddress = server.get(InetSocketAddress.class);

        final CommentedConfigurationNode velocity = config().node("velocity");
        velocitySecret = velocity.node("secret").getString();

        final CommentedConfigurationNode redis = config().node("redis");
        redisAddress = redis.get(InetSocketAddress.class);
        redisPassword = redis.node("password").getString();

        final CommentedConfigurationNode other = config().node("other");
        spawn = other.node("spawn").get(Pos.class);
        gamemode = GameMode.valueOf(other.node("gamemode").getString());
    }

    public String serverName() {
        return serverName;
    }

    public InetSocketAddress serverAddress() {
        return serverAddress;
    }

    public String velocitySecret() {
        return velocitySecret;
    }

    public InetSocketAddress redisAddress() {
        return redisAddress;
    }

    public String redisPassword() {
        return redisPassword;
    }

    public Pos spawn() {
        return spawn;
    }

    public GameMode gamemode() {
        return gamemode;
    }
}
