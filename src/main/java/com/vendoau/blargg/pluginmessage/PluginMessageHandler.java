package com.vendoau.blargg.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerPluginMessageEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class PluginMessageHandler {

    protected final String channel;

    private final Map<String, Queue<CompletableFuture<?>>> callbackMap = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    public PluginMessageHandler(String channel, List<String> subChannels) {
        this.channel = channel;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerPluginMessageEvent.class, event -> {
            if (!event.getIdentifier().equals(channel)) return;

            final ByteArrayDataInput input = ByteStreams.newDataInput(event.getMessage());
            final String subChannel = input.readUTF();
            if (!subChannels.contains(subChannel)) return;

            final Queue<CompletableFuture<?>> callbacks;
            if (subChannel.contains("-")) {
                final String identifier = input.readUTF();
                callbacks = callbackMap.get(subChannel + "-" + identifier);
            } else {
                callbacks = callbackMap.get(subChannel);
            }

            final CompletableFuture<?> callback = callbacks.poll();
            handleCallbacks(input, subChannel, callback);
        });
    }

    protected abstract void handleCallbacks(ByteArrayDataInput input, String subChannel, CompletableFuture<?> callback);

    protected final void addCallback(String key, CompletableFuture<?> future) {
        callbackMap.compute(key, (s, futures) -> {
            if (futures == null) futures = new ArrayDeque<>();
            futures.add(future);
            return futures;
        });
    }
}
