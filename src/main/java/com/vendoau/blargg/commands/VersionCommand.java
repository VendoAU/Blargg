package com.vendoau.blargg.commands;

import com.vendoau.blargg.Blargg;
import net.minestom.server.Git;
import net.minestom.server.command.builder.Command;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version");

        addSyntax((sender, context) -> {
            sender.sendMessage("Blargg: v" + Blargg.version());
            sender.sendMessage("Minestom: " + Git.commit().substring(0, 7));
        });
    }
}
