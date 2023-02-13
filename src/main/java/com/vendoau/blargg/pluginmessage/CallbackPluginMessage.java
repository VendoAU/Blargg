package com.vendoau.blargg.pluginmessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public final class CallbackPluginMessage<T> {

    private final byte[] data;
    private final PluginMessageHandler handler;
    private final String key;

    @SuppressWarnings("UnstableApiUsage")
    public CallbackPluginMessage(String subChannel, PluginMessageHandler handler, String key, String... message) {
        this.handler = handler;
        this.key = key;

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(subChannel);
        Arrays.stream(message).forEach(output::writeUTF);
        this.data = output.toByteArray();
    }

    @SuppressWarnings("UnstableApiUsage")
    public CallbackPluginMessage(String subChannel, PluginMessageHandler handler, String key, byte[] data) {
        this.handler = handler;
        this.key = key;

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(subChannel);
        output.write(data);
        this.data = output.toByteArray();
    }

    public CallbackPluginMessage(String subChannel, PluginMessageHandler handler) {
        this(subChannel, handler, subChannel);
    }

    public CompletableFuture<T> sendMessage(Player player) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        handler.addCallback(key, future);
        player.sendPluginMessage(handler.channel, data);
        return future;
    }

    public byte[] data() {
        return data;
    }

    public String key() {
        return key;
    }
}
