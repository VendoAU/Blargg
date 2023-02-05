package com.vendoau.blargg.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class PacketUtil {

    private PacketUtil() {}

    public static PlayerInfoPacket addPlayerInfoPacket(UUID uuid, String username, @Nullable PlayerSkin skin) {
        final List<PlayerInfoPacket.AddPlayer.Property> properties = new ArrayList<>();
        if (skin != null) {
            properties.add(new PlayerInfoPacket.AddPlayer.Property("textures", skin.textures(), skin.signature()));
        }

        final PlayerInfoPacket.AddPlayer playerEntry = new PlayerInfoPacket.AddPlayer(uuid, username, properties, GameMode.CREATIVE, 0, Component.text(username), null);
        return new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER, Collections.singletonList(playerEntry));
    }

    public static PlayerInfoPacket removePlayerInfoPacket(List<UUID> uuids) {
        final List<PlayerInfoPacket.Entry> entries = new ArrayList<>();
        for (UUID uuid : uuids) {
            entries.add(new PlayerInfoPacket.RemovePlayer(uuid));
        }

        return new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, entries);
    }

    public static PlayerInfoPacket removePlayerInfoPacket(UUID uuid) {
        final PlayerInfoPacket.Entry entry = new PlayerInfoPacket.RemovePlayer(uuid);
        return new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, entry);
    }
}
