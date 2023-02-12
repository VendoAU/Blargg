package com.vendoau.blargg.server;

import com.google.gson.JsonObject;
import com.vendoau.blargg.Blargg;
import com.vendoau.blargg.config.BlarggConfig;
import net.minestom.server.MinecraftServer;

import java.net.InetSocketAddress;

public class ServerInfo {

    private final String name;
    private final InetSocketAddress address;

    private int playerCount;
    private boolean online;

    public ServerInfo(String name, InetSocketAddress address) {
        this.name = name;
        this.address = address;
    }

    public static ServerInfo get() {
        final BlarggConfig config = Blargg.config();
        final String name = config.serverName();
        final InetSocketAddress address = config.serverAddress();

        final ServerInfo serverInfo = new ServerInfo(name, address);
        serverInfo.setPlayerCount(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
        serverInfo.setOnline(true);

        return serverInfo;
    }

    public JsonObject toJson() {
        final JsonObject info = new JsonObject();
        info.addProperty("name", name);
        info.addProperty("ip", address.getHostString());
        info.addProperty("port", address.getPort());
        info.addProperty("playerCount", playerCount);
        info.addProperty("online", online);
        return info;
    }

    public static ServerInfo fromJson(JsonObject json) {
        final String name = json.get("name").getAsString();
        final String ip = json.get("ip").getAsString();
        final int port = json.get("port").getAsInt();
        final InetSocketAddress address = new InetSocketAddress(ip, port);

        final int playerCount = json.get("playerCount").getAsInt();
        final boolean online = json.get("online").getAsBoolean();

        final ServerInfo serverInfo = new ServerInfo(name, address);
        serverInfo.setPlayerCount(playerCount);
        serverInfo.setOnline(online);
        return serverInfo;
    }

    public String name() {
        return name;
    }

    public InetSocketAddress address() {
        return address;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
