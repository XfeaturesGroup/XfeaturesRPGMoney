package xyz.xfeatures.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class FishConfig {
    private final YamlConfiguration config;

    public FishConfig(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "fishes.yml");
        if (!file.exists()) plugin.saveResource("fishes.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public List<Double> getReward(String fish) {
        return config.getDoubleList(fish);
    }
}