package com.vendoau.blargg;

import com.vendoau.blargg.commands.StopCommand;
import com.vendoau.blargg.commands.VersionCommand;
import com.vendoau.blargg.config.BlarggConfig;
import com.vendoau.blargg.generator.SuperFlatGenerator;
import com.vendoau.blargg.util.InstanceUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;

import java.io.IOException;

public class Blargg {

    public static void main(String[] args) {

        final MinecraftServer minecraftServer = MinecraftServer.init();

        final BlarggConfig config = new BlarggConfig();

        final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        final InstanceContainer instance = instanceManager.createInstanceContainer(InstanceUtil.BRIGHT_DIMENSION);
        instance.setGenerator(new SuperFlatGenerator());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(config.spawn());
            player.setGameMode(config.gamemode());
        });

        final CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new StopCommand());
        commandManager.register(new VersionCommand());

        minecraftServer.start(config.ip(), config.port());

        VelocityProxy.enable(config.velocitySecret());
    }

    public static class Properties {

        public static final String VERSION;

        static {
            try {
                final java.util.Properties properties = new java.util.Properties();
                properties.load(Blargg.class.getResourceAsStream("/blargg.properties"));
                VERSION = properties.getProperty("version");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
