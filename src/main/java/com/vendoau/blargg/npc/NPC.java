package com.vendoau.blargg.npc;

import com.vendoau.blargg.util.PacketUtil;
import com.vendoau.blargg.util.TeamUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;

public class NPC extends LivingEntity {

    private final String username;

    public NPC(Instance instance, Pos pos, String username) {
        super(EntityType.PLAYER);
        this.username = username;

        setInstance(instance, pos);
        TeamUtil.NPC_TEAM.addMember(username);

        MinecraftServer.getGlobalEventHandler().addListener(PlayerEntityInteractEvent.class, event -> {
            if (event.getHand() == Player.Hand.OFF) return;

            final Player player = event.getPlayer();
            final Entity target = event.getTarget();
            if (target.getUuid().equals(uuid)) {
                onInteract(player);
            }
        });
    }

    protected void onInteract(Player player) {}

    public void sendToPlayer(Player player) {
        player.sendPacket(PacketUtil.addPlayerInfoPacket(uuid, username, null));
        addViewer(player);
    }

    public void removeFromTab(Player player) {
        player.sendPacket(PacketUtil.removePlayerInfoPacket(uuid));
    }
}