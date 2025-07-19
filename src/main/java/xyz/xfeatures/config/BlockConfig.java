package xyz.xfeatures.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class BlockConfig {
    private final YamlConfiguration config;

    public BlockConfig(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "blocks.yml");
        if (!file.exists()) plugin.saveResource("blocks.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public List<Double> getReward(String block) {
        return config.getDoubleList(block);
    }
}