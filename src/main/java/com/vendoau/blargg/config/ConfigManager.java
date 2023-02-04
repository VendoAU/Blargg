package com.vendoau.blargg.config;

import com.vendoau.blargg.config.serializer.PosSerializer;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class ConfigManager {

    private final File configFile;
    private final String defaultConfig;

    private CommentedConfigurationNode config;

    private static final TypeSerializerCollection SERIALIZERS = TypeSerializerCollection.builder()
            .register(Pos.class, PosSerializer.INSTANCE)
            .build();

    public ConfigManager(File configFile, @Nullable String defaultConfig) {
        this.configFile = configFile;
        this.defaultConfig = defaultConfig;
        try {
            load();
        } catch (IOException e) {
            System.err.println("An error occurred while trying to load the config");
            e.printStackTrace();
        }
    }

    public ConfigManager(File configFile) {
        this(configFile, null);
    }

    protected void load() throws IOException {
        if (!configFile.exists() && defaultConfig != null) {
            if (configFile.getParentFile() != null) {
                configFile.getParentFile().mkdirs();
            }
            Files.copy(getClass().getResourceAsStream(defaultConfig), configFile.toPath());
        }

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .file(configFile)
                .defaultOptions(defaultOptions -> defaultOptions.serializers(SERIALIZERS))
                .build();
        config = loader.load();
    }

    public void reload() {
        try {
            load();
        } catch (IOException e) {
            System.err.println("An error occurred while trying to reload the config");
            e.printStackTrace();
        }
    }

    public final CommentedConfigurationNode config() {
        return config;
    }
}
