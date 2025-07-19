package xyz.xfeatures.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MessagesConfig {
    private final YamlConfiguration config;

    public MessagesConfig(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String key) {
        return config.getString(key, key);
    }
}