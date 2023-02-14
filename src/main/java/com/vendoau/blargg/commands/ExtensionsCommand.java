package com.vendoau.blargg.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExtensionsCommand extends Command {

    public ExtensionsCommand() {
        super("extensions");

        addSyntax((sender, context) -> {
            final Collection<Extension> extensions = MinecraftServer.getExtensionManager().getExtensions();
            sender.sendMessage("Extensions (" + extensions.size() + "):");

            final List<Component> extensionComponents = new ArrayList<>();
            for (Extension extension : extensions) {
                final DiscoveredExtension origin = extension.getOrigin();
                final String name = origin.getName();
                final String version = origin.getVersion();
                final String[] authors = origin.getAuthors();
                final String[] dependencies = origin.getDependencies();

                Component hoverComponent = Component.text(name)
                        .appendNewline()
                        .append(Component.text("Version: " + version));

                if (authors.length == 1) {
                    hoverComponent = hoverComponent
                            .appendNewline()
                            .append(Component.text("Author: " + authors[0]));
                } else if (authors.length != 0) {
                    hoverComponent = hoverComponent
                            .appendNewline()
                            .append(Component.text("Authors: " + String.join(", ", authors)));
                }

                if (dependencies.length != 0) {
                    hoverComponent = hoverComponent
                            .appendNewline()
                            .append(Component.text("Dependencies: " + String.join(", ", dependencies)));
                }

                final Component extensionComponent = Component.text(name)
                        .hoverEvent(hoverComponent);
                extensionComponents.add(extensionComponent);
            }

            sender.sendMessage(Component.join(JoinConfiguration.commas(true), extensionComponents));
        });
    }
}
