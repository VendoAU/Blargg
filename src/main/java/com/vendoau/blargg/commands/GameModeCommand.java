package com.vendoau.blargg.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class GameModeCommand extends Command {

    public GameModeCommand() {
        super("gamemode");

        // Allow players to use gamemode shortcuts (F3+F4, F3+N)
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setPermissionLevel(4);
        });

        final ArgumentEntity playerArgument = ArgumentType.Entity("player")
                .onlyPlayers(true);
        final Argument<GameMode> gameModeArgument = ArgumentType.Enum("gamemode", GameMode.class)
                .setFormat(ArgumentEnum.Format.LOWER_CASED);

        addConditionalSyntax((sender, commandString) -> {
            return sender instanceof Player;
        }, (sender, context) -> {
            final GameMode gameMode = context.get(gameModeArgument);
            final Player player = (Player) sender;
            player.setGameMode(gameMode);
        }, gameModeArgument);

        addSyntax((sender, context) -> {
            final GameMode gameMode = context.get(gameModeArgument);
            final var players = context.get(playerArgument);
            players.find(sender).forEach(entity -> {
                final Player player = (Player) entity;
                player.setGameMode(gameMode);
            });
        }, gameModeArgument, playerArgument);
    }
}
