package xyz.xfeatures.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcheologyConfig {
    private final YamlConfiguration config;
    private final Map<Material, double[]> rewards = new HashMap<>();

    public ArcheologyConfig(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "archeology.yml");
        if (!file.exists()) plugin.saveResource("archeology.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
        loadRewards();
    }

    private void loadRewards() {
        rewards.clear();
        for (String key : config.getKeys(false)) {
            try {
                Material material = Material.valueOf(key);
                List<Double> values = config.getDoubleList(key);
                if (values.size() >= 2) {
                    rewards.put(material, new double[]{values.get(0), values.get(1)});
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public void reload() {
        loadRewards();
    }

    public boolean hasReward(Material material) {
        return rewards.containsKey(material);
    }

    public double[] getReward(Material material) {
        return rewards.get(material);
    }
}