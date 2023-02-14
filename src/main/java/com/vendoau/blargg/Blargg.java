package com.vendoau.blargg;

import com.vendoau.blargg.commands.ExtensionsCommand;
import com.vendoau.blargg.commands.GameModeCommand;
import com.vendoau.blargg.commands.StopCommand;
import com.vendoau.blargg.commands.VersionCommand;
import com.vendoau.blargg.config.BlarggConfig;
import com.vendoau.blargg.generator.SuperFlatGenerator;
import com.vendoau.blargg.pluginmessage.BungeeMessageHandler;
import com.vendoau.blargg.util.InstanceUtil;
import com.vendoau.blargg.util.RedisUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import net.minestom.server.timer.TaskSchedule;

import java.io.IOException;
import java.util.Properties;

public class Blargg {

    private static String version;
    private static BlarggConfig config;
    private static BungeeMessageHandler bungeeMessageHandler;

    public static void main(String[] args) {
        try {
            final Properties properties = new Properties();
            properties.load(Blargg.class.getResourceAsStream("/blargg.properties"));
            version = properties.getProperty("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config = new BlarggConfig();

        final MinecraftServer minecraftServer = MinecraftServer.init();
        VelocityProxy.enable(config.velocitySecret());

        bungeeMessageHandler = new BungeeMessageHandler();

        // Instance
        final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        final InstanceContainer instance = instanceManager.createInstanceContainer(InstanceUtil.BRIGHT_DIMENSION);
        instance.setGenerator(new SuperFlatGenerator());

        // Commands
        final CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new ExtensionsCommand());
        commandManager.register(new GameModeCommand());
        commandManager.register(new StopCommand());
        commandManager.register(new VersionCommand());
        commandManager.setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage(Component.text("Unknown command.", NamedTextColor.RED));
        });

        // Login
        final GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(config.spawn());
            player.setGameMode(config.gamemode());
        });

        // Redis
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            RedisUtil.publishServerInfo();
        }).addListener(PlayerDisconnectEvent.class, event -> {
            RedisUtil.publishServerInfo();
        });
        final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
        schedulerManager.buildTask(RedisUtil::publishServerInfo)
                .repeat(TaskSchedule.seconds(5))
                .schedule();

        // Shutdown
        schedulerManager.buildShutdownTask(RedisUtil::publishOfflineServerInfo);

        minecraftServer.start(config.serverAddress());
    }

    public static String version() {
        return version;
    }

    public static BlarggConfig config() {
        return config;
    }

    public static BungeeMessageHandler bungeeMessageHandler() {
        return bungeeMessageHandler;
    }
}
