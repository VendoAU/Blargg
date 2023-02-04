package com.vendoau.blargg.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        addSyntax((sender, context) -> {
            MinecraftServer.stopCleanly();
        });
    }
}
