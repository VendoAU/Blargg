package com.vendoau.blargg.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class BungeeMessageHandler extends PluginMessageHandler {

    public BungeeMessageHandler() {
        super("bungeecord:main", List.of(
                "Connect",
                "ConnectOther",
                "IP",
                "IPOther",
                "PlayerCount",
                "PlayerList",
                "GetServers",
                "Message",
                "MessageRaw",
                "GetServer",
                "Forward",
                "ForwardToPlayer",
                "UUID",
                "UUIDOther",
                "ServerIP",
                "KickPlayer"
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleCallbacks(ByteArrayDataInput input, String subChannel, CompletableFuture<?> callback) {
        switch (subChannel) {
            case "IP", "IPOther" -> {
                final String ip = input.readUTF();
                final int port = input.readInt();
                final InetSocketAddress address = new InetSocketAddress(ip, port);
                ((CompletableFuture<InetSocketAddress>) callback).complete(address);
            }
            case "ServerIP" -> {
                final String ip = input.readUTF();
                final int port = input.readUnsignedShort();
                final InetSocketAddress address = new InetSocketAddress(ip, port);
                ((CompletableFuture<InetSocketAddress>) callback).complete(address);
            }
            case "PlayerCount" -> {
                final int playerCount = input.readInt();
                ((CompletableFuture<Integer>) callback).complete(playerCount);
            }
            case "PlayerList", "GetServers" -> {
                final String[] strings = input.readUTF().split(", ");
                ((CompletableFuture<String[]>) callback).complete(strings);
            }
            case "GetServer" -> {
                final String server = input.readUTF();
                ((CompletableFuture<String>) callback).complete(server);
            }
            case "UUID", "UUIDOther" -> {
                final UUID uuid = UUID.fromString(input.readUTF());
                ((CompletableFuture<UUID>) callback).complete(uuid);
            }
            // TODO: Forward, ForwardToPlayer
        }
    }

    public PluginMessage connect(String server) {
        return new PluginMessage("Connect", this, server);
    }

    public PluginMessage connectOther(String player, String server) {
        return new PluginMessage("ConnectOther", this, player, server);
    }

    public CallbackPluginMessage<InetSocketAddress> ip() {
        return new CallbackPluginMessage<>("IP", this);
    }

    public CallbackPluginMessage<InetSocketAddress> ipOther(String player) {
        return new CallbackPluginMessage<>("IPOther", this, "IPOther-" + player, player);
    }

    public CallbackPluginMessage<Integer> playerCount(String server) {
        return new CallbackPluginMessage<>("PlayerCount", this, "PlayerCount-" + server, server);
    }

    public CallbackPluginMessage<String[]> playerList(String server) {
        return new CallbackPluginMessage<>("PlayerList", this, "PlayerList-" + server, server);
    }

    public CallbackPluginMessage<String[]> getServers() {
        return new CallbackPluginMessage<>("GetServers", this);
    }

    public PluginMessage message(String player, String message) {
        return new PluginMessage("Message", this, player, message);
    }

    public PluginMessage messageRaw(String player, String message) {
        return new PluginMessage("MessageRaw", this, player, message);
    }

    public CallbackPluginMessage<String> getServer() {
        return new CallbackPluginMessage<>("GetServer", this);
    }

    @SuppressWarnings("UnstableApiUsage")
    public PluginMessage forward(String server, PluginMessage forwardedMessage) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(server);
        output.write(forwardedMessage.data());

        return new PluginMessage("Forward", this, output.toByteArray());
    }

    @SuppressWarnings("UnstableApiUsage")
    public <T> CallbackPluginMessage<CallbackPluginMessage<T>> forward(String server, CallbackPluginMessage<T> forwardedMessage) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(server);
        output.write(forwardedMessage.data());

        return new CallbackPluginMessage<>("Forward", this, forwardedMessage.key(), output.toByteArray());
    }

    @SuppressWarnings("UnstableApiUsage")
    public PluginMessage forwardToPlayer(String player, PluginMessage forwardedMessage) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(player);
        output.write(forwardedMessage.data());

        return new PluginMessage("ForwardToPlayer", this, output.toByteArray());
    }

    @SuppressWarnings("UnstableApiUsage")
    public <T> CallbackPluginMessage<CallbackPluginMessage<T>> forwardToPlayer(String player, CallbackPluginMessage<T> forwardedMessage) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(player);
        output.write(forwardedMessage.data());

        return new CallbackPluginMessage<>("ForwardToPlayer", this, forwardedMessage.key(), output.toByteArray());
    }

    public CallbackPluginMessage<UUID> uuid() {
        return new CallbackPluginMessage<>("UUID", this);
    }

    public CallbackPluginMessage<UUID> uuidOther(String player) {
        return new CallbackPluginMessage<>("UUIDOther", this, "UUIDOther-" + player, player);
    }

    public CallbackPluginMessage<InetSocketAddress> serverIP(String server) {
        return new CallbackPluginMessage<>("ServerIP", this, "ServerIP-" + server, server);
    }

    public PluginMessage kickPlayer(String player) {
        return new PluginMessage("KickPlayer", this, player);
    }
}
