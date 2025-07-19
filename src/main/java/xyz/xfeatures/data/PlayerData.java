package xyz.xfeatures.data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.xfeatures.XfeaturesRPGMoney;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerData {
    private final XfeaturesRPGMoney plugin;
    private final Map<UUID, Double> collectedMoney = new HashMap<>();
    private final Map<UUID, String> playerNames = new HashMap<>();
    private final File dataFile;
    private FileConfiguration dataConfig;
    
    public PlayerData(XfeaturesRPGMoney plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        loadData();
    }
    
    public void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать файл данных игроков: " + e.getMessage());
            }
        }
        
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        if (dataConfig.contains("collected-money")) {
            for (String uuidStr : dataConfig.getConfigurationSection("collected-money").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    double amount = dataConfig.getDouble("collected-money." + uuidStr);
                    collectedMoney.put(uuid, amount);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Неверный формат UUID в данных игрока: " + uuidStr);
                }
            }
        }

        if (dataConfig.contains("player-names")) {
            for (String uuidStr : dataConfig.getConfigurationSection("player-names").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String name = dataConfig.getString("player-names." + uuidStr);
                    playerNames.put(uuid, name);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Неверный формат UUID в данных имен игроков: " + uuidStr);
                }
            }
        }
    }
    
    public void saveData() {
        for (Map.Entry<UUID, Double> entry : collectedMoney.entrySet()) {
            dataConfig.set("collected-money." + entry.getKey().toString(), entry.getValue());
        }

        for (Map.Entry<UUID, String> entry : playerNames.entrySet()) {
            dataConfig.set("player-names." + entry.getKey().toString(), entry.getValue());
        }
        
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить данные игроков: " + e.getMessage());
        }
    }
    
    public void addCollectedMoney(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double current = collectedMoney.getOrDefault(uuid, 0.0);
        collectedMoney.put(uuid, current + amount);

        playerNames.put(uuid, player.getName());
    }
    
    public double getCollectedMoney(Player player) {
        return collectedMoney.getOrDefault(player.getUniqueId(), 0.0);
    }
    
    public List<PlayerStats> getTopPlayers(int limit, int offset) {
        List<PlayerStats> stats = collectedMoney.entrySet().stream()
            .map(entry -> new PlayerStats(playerNames.getOrDefault(entry.getKey(), "Unknown"), entry.getValue()))
            .sorted(Comparator.comparingDouble(PlayerStats::getMoney).reversed())
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
        return stats;
    }

    public static class PlayerStats {
        private final String playerName;
        private final double money;

        public PlayerStats(String playerName, double money) {
            this.playerName = playerName;
            this.money = money;
        }

        public String getPlayerName() {
            return playerName;
        }

        public double getMoney() {
            return money;
        }
    }
}