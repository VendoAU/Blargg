package com.vendoau.blargg.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;

public final class TeamUtil {

    public static final Team NPC_TEAM;

    private TeamUtil() {}

    static {
        NPC_TEAM = MinecraftServer.getTeamManager().createTeam("NPC_TEAM");
        NPC_TEAM.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }
}
