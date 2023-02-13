package com.vendoau.blargg.pluginmessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;

import java.util.Arrays;

public final class PluginMessage {

    private final byte[] data;
    private final PluginMessageHandler handler;

    @SuppressWarnings("UnstableApiUsage")
    public PluginMessage(String subChannel, PluginMessageHandler handler, String... message) {
        this.handler = handler;

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(subChannel);
        Arrays.stream(message).forEach(output::writeUTF);
        this.data = output.toByteArray();
    }

    @SuppressWarnings("UnstableApiUsage")
    public PluginMessage(String subChannel, PluginMessageHandler handler, byte[] data) {
        this.handler = handler;

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(subChannel);
        output.write(data);
        this.data = output.toByteArray();
    }

    public void sendMessage(Player player) {
        player.sendPluginMessage(handler.channel, data);
    }

    public byte[] data() {
        return data;
    }
}
