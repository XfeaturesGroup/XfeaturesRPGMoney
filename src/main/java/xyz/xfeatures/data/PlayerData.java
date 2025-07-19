package xyz.xfeatures.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.xfeatures.XfeaturesRPGMoney;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PlayerData {
    private final XfeaturesRPGMoney plugin;
    private final Map<UUID, Double> collectedMoney = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerNames = new ConcurrentHashMap<>();
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
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().warning("Could not create playerdata.yml");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create playerdata.yml", e);
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        collectedMoney.clear();
        playerNames.clear();

        dataConfig.getKeys(false).stream()
                .filter(this::isValidUUID)
                .forEach(uuidStr -> {
                    UUID uuid = UUID.fromString(uuidStr);
                    String name = dataConfig.getString(uuidStr + ".name", "Unknown");
                    double money = dataConfig.getDouble(uuidStr + ".money", 0.0);

                    collectedMoney.put(uuid, money);
                    playerNames.put(uuid, name);
                });
    }

    private boolean isValidUUID(String key) {
        try {
            UUID.fromString(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void saveData() {
        dataConfig = new YamlConfiguration();

        collectedMoney.forEach((uuid, money) -> {
            String path = uuid.toString();
            dataConfig.set(path + ".money", money);
            dataConfig.set(path + ".name", playerNames.getOrDefault(uuid, "Unknown"));
        });

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save playerdata.yml", e);
        }
    }

    public void addCollectedMoney(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        collectedMoney.compute(uuid, (key, value) -> (value == null ? 0 : value) + amount);
        playerNames.put(uuid, player.getName());
    }

    public double getCollectedMoney(Player player) {
        return collectedMoney.getOrDefault(player.getUniqueId(), 0.0);
    }

    public List<PlayerStats> getTopPlayers(int limit, int offset) {
        return collectedMoney.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .skip(offset)
                .limit(limit)
                .map(entry -> new PlayerStats(
                        playerNames.getOrDefault(entry.getKey(), "Unknown"),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerStats that = (PlayerStats) o;
            return Double.compare(that.money, money) == 0 &&
                    Objects.equals(playerName, that.playerName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerName, money);
        }

        @Override
        public String toString() {
            return playerName + ": " + money;
        }
    }
}