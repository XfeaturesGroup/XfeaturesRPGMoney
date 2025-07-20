package xyz.xfeatures.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.data.PlayerData.PlayerStats;
import xyz.xfeatures.util.MoneyUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * API для взаимодействия с функциональностью XfeaturesRPGMoney.
 * Используйте этот класс для интеграции с другими плагинами.
 * 
 * @author XfeaturesGroup, kingnoype
 * @version 1.1.0
 */
public class RPGMoneyAPI {

    private static volatile RPGMoneyAPI instance;
    private final XfeaturesRPGMoney plugin;

    private RPGMoneyAPI(XfeaturesRPGMoney plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin instance cannot be null");
    }

    /**
     * Получает экземпляр API (потокобезопасный Singleton).
     *
     * @return Экземпляр API
     * @throws IllegalStateException если плагин не инициализирован
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
     * Выбрасывает деньги в указанном месте.
     *
     * @param location место выпадения денег
     * @param amount сумма денег
     */
    public void dropMoney(Location location, double amount) {
        MoneyUtil.dropMoney(location, amount);
    }

    /**
     * Получает случайное значение между минимальным и максимальным.
     *
     * @param min минимальное значение
     * @param max максимальное значение
     * @return случайное значение в указанном диапазоне
     * @throws IllegalArgumentException если min > max
     */
    public double getRandomInRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        return MoneyUtil.getRandomInRange(min, max);
    }

    /**
     * Проверяет, был ли блок в указанном месте размещен игроком.
     *
     * @param location местоположение блока
     * @return true если блок был размещен игроком, иначе false
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
     * Получает множитель удачи для указанного уровня.
     *
     * @param level уровень зачарования Fortune
     * @return множитель удачи
     */
    public double getFortuneMultiplier(int level) {
        return plugin.mainConfig.getFortuneMultiplier(level);
    }

    /**
     * Получает множитель добычи для указанного уровня.
     *
     * @param level уровень зачарования Looting
     * @return множитель добычи
     */
    public double getLootingMultiplier(int level) {
        return plugin.mainConfig.getLootingMultiplier(level);
    }

    /**
     * Получает шанс выпадения денег.
     *
     * @return шанс выпадения денег (от 0.0 до 1.0)
     */
    public double getDropChance() {
        return plugin.mainConfig.getDropChance();
    }

    /**
     * Проверяет, включены ли сообщения в action bar.
     *
     * @return true если сообщения в action bar включены, иначе false
     */
    public boolean isShowActionBarMessages() {
        return plugin.mainConfig.isShowActionBarMessages();
    }

    /**
     * Получает список лучших игроков по собранным монетам.
     *
     * @param limit максимальное количество игроков в списке
     * @param offset смещение от начала списка
     * @return список статистики игроков
     */
    public List<PlayerStats> getTopPlayers(int limit, int offset) {
        return plugin.playerData.getTopPlayers(limit, offset);
    }

    /**
     * Получает максимальное количество денег, которое может выпасть.
     *
     * @return максимальная сумма выпадения
     */
    public double getMaxMoneyDrop() {
        return plugin.mainConfig.getMaxMoneyDrop();
    }

    /**
     * Проверяет на наличие награды за археологию для материала.
     *
     * @param material материал для проверки
     * @return true если есть награда за археологию, иначе false
     */
    public boolean hasArcheologyReward(Material material) {
        return plugin.archeologyConfig.hasReward(material);
    }

    /**
     * Получает награду за археологию для материала.
     *
     * @param material материал для получения награды
     * @return массив с минимальным и максимальным значением награды
     */
    public double[] getArcheologyReward(Material material) {
        return plugin.archeologyConfig.getReward(material);
    }
}