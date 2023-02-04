package com.vendoau.blargg.config;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ConfigManager {

    private final File configFile;
    private final String defaultConfig;

    private CommentedConfigurationNode config;

    public ConfigManager(File configFile, @Nullable String defaultConfig) {
        this.configFile = configFile;
        this.defaultConfig = defaultConfig;
        load();
    }

    public ConfigManager(File configFile) {
        this(configFile, null);
    }

    public void load() {
        if (!configFile.exists() && defaultConfig != null) {
            try {
                if (configFile.getParentFile() != null) {
                    configFile.getParentFile().mkdirs();
                }

                Files.copy(getClass().getResourceAsStream(defaultConfig), configFile.toPath());
            } catch (IOException e) {
                System.err.println("An error occurred while trying to copy default config: \"" + configFile.getPath() + "\", from: \"" + defaultConfig + "\"");
                e.printStackTrace();
            }
        }

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().file(configFile).build();
        try {
            config = loader.load();
        } catch (ConfigurateException e) {
            System.err.println("An error occurred while trying to load config: \"" + configFile.getPath() + "\"");
            e.printStackTrace();
        }
    }

    public CommentedConfigurationNode config() {
        return config;
    }
}
