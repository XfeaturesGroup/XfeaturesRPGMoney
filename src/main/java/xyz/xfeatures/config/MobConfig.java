package xyz.xfeatures.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class MobConfig {
    private final YamlConfiguration config;

    public MobConfig(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "mobs.yml");
        if (!file.exists()) plugin.saveResource("mobs.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public List<Double> getReward(String mob) {
        return config.getDoubleList(mob);
    }
}