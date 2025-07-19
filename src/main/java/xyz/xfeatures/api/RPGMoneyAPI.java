package xyz.xfeatures.api;

import org.bukkit.Location;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;

public class RPGMoneyAPI {
    
    private static RPGMoneyAPI instance;
    private final XfeaturesRPGMoney plugin;
    
    private RPGMoneyAPI(XfeaturesRPGMoney plugin) {
        this.plugin = plugin;
    }
    
    public static RPGMoneyAPI getInstance() {
        if (instance == null) {
            instance = new RPGMoneyAPI(XfeaturesRPGMoney.instance);
        }
        return instance;
    }
    
    /**
     * Drops money at the specified location
     * 
     * @param location The location to drop money at
     * @param amount The amount of money to drop
     */
    public void dropMoney(Location location, double amount) {
        MoneyUtil.dropMoney(location, amount);
    }
    
    /**
     * Gets a random value between min and max
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @return A random value between min and max
     */
    public double getRandomInRange(double min, double max) {
        return MoneyUtil.getRandomInRange(min, max);
    }
    
    /**
     * Checks if a block at the specified location was placed by a player
     * 
     * @param location The location to check
     * @return True if the block was placed by a player, false otherwise
     */
    public boolean isPlayerPlacedBlock(Location location) {
        return location.getChunk().getPersistentDataContainer().has(
                plugin.getNameKey(),
                org.bukkit.persistence.PersistentDataType.BOOLEAN
        );
    }
    
    /**
     * Gets the fortune multiplier for the specified level
     * 
     * @param level The fortune level
     * @return The multiplier for the specified level
     */
    public double getFortuneMultiplier(int level) {
        return plugin.mainConfig.getFortuneMultiplier(level);
    }
    
    /**
     * Gets the looting multiplier for the specified level
     * 
     * @param level The looting level
     * @return The multiplier for the specified level
     */
    public double getLootingMultiplier(int level) {
        return plugin.mainConfig.getLootingMultiplier(level);
    }
}