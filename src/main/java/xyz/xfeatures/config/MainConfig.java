package xyz.xfeatures.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MainConfig {
    private final Plugin plugin;
    private double dropChance;
    private boolean showActionBarMessages;
    private double playerDeathDropPercentage;
    private Map<Integer, Double> fortuneMultipliers;
    private Map<Integer, Double> lootingMultipliers;
    private double maxMoneyDrop;
    private boolean combineNearbyDrops;
    private double combineRadius;

    public MainConfig(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        if (!config.contains("drop-chance")) {
            config.set("drop-chance", 0.75);
        }
        
        if (!config.contains("show-action-bar-messages")) {
            config.set("show-action-bar-messages", true);
        }
        
        if (!config.contains("player-death-drop-percentage")) {
            config.set("player-death-drop-percentage", 0.07);
        }

        if (!config.contains("fortune-multipliers.1")) {
            config.set("fortune-multipliers.1", 1.25);
        }
        if (!config.contains("fortune-multipliers.2")) {
            config.set("fortune-multipliers.2", 1.5);
        }
        if (!config.contains("fortune-multipliers.3")) {
            config.set("fortune-multipliers.3", 1.75);
        }

        if (!config.contains("looting-multipliers.1")) {
            config.set("looting-multipliers.1", 1.25);
        }
        if (!config.contains("looting-multipliers.2")) {
            config.set("looting-multipliers.2", 1.5);
        }
        if (!config.contains("looting-multipliers.3")) {
            config.set("looting-multipliers.3", 1.75);
        }

        if (!config.contains("max-money-drop")) {
            config.set("max-money-drop", 1000.0);
        }
        
        if (!config.contains("combine-nearby-drops")) {
            config.set("combine-nearby-drops", true);
        }
        
        if (!config.contains("combine-radius")) {
            config.set("combine-radius", 1.5);
        }
        
        if (!config.contains("spawner-multiplier")) {
            config.set("spawner-multiplier", 0.6);
        }
        
        plugin.saveConfig();

        dropChance = config.getDouble("drop-chance");
        showActionBarMessages = config.getBoolean("show-action-bar-messages");
        playerDeathDropPercentage = config.getDouble("player-death-drop-percentage");
        maxMoneyDrop = config.getDouble("max-money-drop");
        combineNearbyDrops = config.getBoolean("combine-nearby-drops");
        combineRadius = config.getDouble("combine-radius");

        fortuneMultipliers = new HashMap<>();
        for (String key : config.getConfigurationSection("fortune-multipliers").getKeys(false)) {
            try {
                int level = Integer.parseInt(key);
                double multiplier = config.getDouble("fortune-multipliers." + key);
                fortuneMultipliers.put(level, multiplier);
            } catch (NumberFormatException ignored) {
                plugin.getLogger().warning("Неверный формат уровня удачи в конфигурации: " + key);
            }
        }

        lootingMultipliers = new HashMap<>();
        for (String key : config.getConfigurationSection("looting-multipliers").getKeys(false)) {
            try {
                int level = Integer.parseInt(key);
                double multiplier = config.getDouble("looting-multipliers." + key);
                lootingMultipliers.put(level, multiplier);
            } catch (NumberFormatException ignored) {
                plugin.getLogger().warning("Неверный формат уровня добычи в конфигурации: " + key);
            }
        }
    }

    public double getDropChance() {
        return dropChance;
    }

    public boolean isShowActionBarMessages() {
        return showActionBarMessages;
    }

    public double getPlayerDeathDropPercentage() {
        return playerDeathDropPercentage;
    }
    
    public double getFortuneMultiplier(int level) {
        return fortuneMultipliers.getOrDefault(level, 1.0);
    }
    
    public double getLootingMultiplier(int level) {
        return lootingMultipliers.getOrDefault(level, 1.0);
    }
    
    public double getMaxMoneyDrop() {
        return maxMoneyDrop;
    }
    
    public boolean isCombineNearbyDrops() {
        return combineNearbyDrops;
    }
    
    public double getCombineRadius() {
        return combineRadius;
    }
    
    public double getSpawnerMultiplier() {
        return plugin.getConfig().getDouble("spawner-multiplier", 0.6);
    }
}