package xyz.xfeatures.api;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.data.PlayerData.PlayerStats;
import xyz.xfeatures.util.MoneyUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * API для взаимодействия с функциональностью XfeaturesRPGMoney
 * Используйте этот класс для интеграции с другими плагинами
 */
public class RPGMoneyAPI {

    private static volatile RPGMoneyAPI instance;
    private final XfeaturesRPGMoney plugin;

    private RPGMoneyAPI(XfeaturesRPGMoney plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin instance cannot be null");
    }

    /**
     * Получает экземпляр API (потокобезопасный Singleton)
     */
    public static RPGMoneyAPI getInstance() {
        if (instance == null) {
            synchronized (RPGMoneyAPI.class) {
                if (instance == null) {
                    XfeaturesRPGMoney pluginInstance = XfeaturesRPGMoney.instance;
                    if (pluginInstance == null) {
                        throw new IllegalStateException("Plugin is not initialized yet");
                    }
                    instance = new RPGMoneyAPI(pluginInstance);
                }
            }
        }
        return instance;
    }

    /**
     * Выбрасывает деньги в указанном месте
     */
    public void dropMoney(Location location, double amount) {
        MoneyUtil.dropMoney(location, amount);
    }

    /**
     * Получает случайное значение между минимальным и максимальным
     */
    public double getRandomInRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        return MoneyUtil.getRandomInRange(min, max);
    }

    /**
     * Проверяет, был ли блок в указанном месте размещен игроком
     */
    public boolean isPlayerPlacedBlock(Location location) {
        if (location == null || location.getChunk() == null) {
            return false;
        }

        return location.getChunk().getPersistentDataContainer().has(
                plugin.getNameKey(),
                PersistentDataType.BOOLEAN
        );
    }

    /**
     * Получает множитель удачи для указанного уровня
     */
    public double getFortuneMultiplier(int level) {
        return plugin.mainConfig.getFortuneMultiplier(level);
    }

    /**
     * Получает множитель добычи для указанного уровня
     */
    public double getLootingMultiplier(int level) {
        return plugin.mainConfig.getLootingMultiplier(level);
    }

    /**
     * Получает шанс выпадения денег
     */
    public double getDropChance() {
        return plugin.mainConfig.getDropChance();
    }

    /**
     * Проверяет, включены ли сообщения в action bar
     */
    public boolean isShowActionBarMessages() {
        return plugin.mainConfig.isShowActionBarMessages();
    }

    /**
     * Получает список лучших игроков по собранным монетам
     */
    public List<PlayerStats> getTopPlayers(int limit, int offset) {
        return plugin.playerData.getTopPlayers(limit, offset);
    }

    /**
     * Получает максимальное количество денег, которое может выпасть
     */
    public double getMaxMoneyDrop() {
        return plugin.mainConfig.getMaxMoneyDrop();
    }
}