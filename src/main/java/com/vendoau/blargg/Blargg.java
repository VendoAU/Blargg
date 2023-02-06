package com.vendoau.blargg;

import com.vendoau.blargg.commands.StopCommand;
import com.vendoau.blargg.commands.VersionCommand;
import com.vendoau.blargg.config.BlarggConfig;
import com.vendoau.blargg.generator.SuperFlatGenerator;
import com.vendoau.blargg.util.InstanceUtil;
import com.vendoau.blargg.util.RedisUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.timer.SchedulerManager;

import java.io.IOException;
import java.util.Properties;

public class Blargg {

    private static String version;

    private static BlarggConfig config;

    public static void main(String[] args) {
        new Blargg();
    }

    public Blargg() {
        try {
            final Properties properties = new Properties();
            properties.load(Blargg.class.getResourceAsStream("/blargg.properties"));
            version = properties.getProperty("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config = new BlarggConfig();

        final MinecraftServer minecraftServer = MinecraftServer.init();
        minecraftServer.start(config.serverAddress());
        VelocityProxy.enable(config.velocitySecret());

        // Instance
        final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        final InstanceContainer instance = instanceManager.createInstanceContainer(InstanceUtil.BRIGHT_DIMENSION);
        instance.setGenerator(new SuperFlatGenerator());

        // Commands
        final CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new StopCommand());
        commandManager.register(new VersionCommand());

        // Login
        final GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(config.spawn());
            player.setGameMode(config.gamemode());
        });

        // Redis
        RedisUtil.publishServerInfo();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            RedisUtil.publishServerInfo();
        }).addListener(PlayerDisconnectEvent.class, event -> {
            RedisUtil.publishServerInfo();
        });

        // Shutdown
        final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
        schedulerManager.buildShutdownTask(RedisUtil::publishOfflineServerInfo);
    }

    public static String version() {
        return version;
    }

    public static BlarggConfig config() {
        return config;
    }
}
