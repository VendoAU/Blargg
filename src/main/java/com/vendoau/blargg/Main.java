package com.vendoau.blargg;

import com.vendoau.blargg.config.ConfigManager;
import com.vendoau.blargg.generator.SuperFlatGenerator;
import com.vendoau.blargg.util.InstanceUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        final MinecraftServer minecraftServer = MinecraftServer.init();

        final ConfigManager configManager = new ConfigManager(new File("blargg.conf"), "/blargg.conf");
        final CommentedConfigurationNode config = configManager.config();

        final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        final InstanceContainer instance = instanceManager.createInstanceContainer(InstanceUtil.BRIGHT_DIMENSION);
        instance.setGenerator(new SuperFlatGenerator());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(new Pos(0, -60, 0));
        });

        final String secret = config.node("velocity-secret").getString();
        VelocityProxy.enable(secret);
        MojangAuth.init();

        final String ip = config.node("server-ip").getString();
        final int port = config.node("server-port").getInt();
        minecraftServer.start(ip, port);
    }
}
