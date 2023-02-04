package com.vendoau.blargg.config;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;

import java.io.File;
import java.io.IOException;

public class BlarggConfig extends ConfigManager {

    private String ip;
    private int port;
    private String velocitySecret;
    private Pos spawn;
    private GameMode gamemode;

    public BlarggConfig() {
        super(new File("blargg.conf"), "/blargg.conf");
    }

    @Override
    public void load() throws IOException {
        super.load();

        ip = config().node("server-ip").getString();
        port = config().node("server-port").getInt();
        velocitySecret = config().node("velocity-secret").getString();
        spawn = config().node("spawn").get(Pos.class);
        gamemode = GameMode.valueOf(config().node("gamemode").getString());
    }

    public String ip() {
        return ip;
    }

    public int port() {
        return port;
    }

    public String velocitySecret() {
        return velocitySecret;
    }

    public Pos spawn() {
        return spawn;
    }

    public GameMode gamemode() {
        return gamemode;
    }
}
