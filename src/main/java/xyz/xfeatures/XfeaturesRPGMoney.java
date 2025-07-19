package xyz.xfeatures;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xfeatures.command.RPGMoneyCommand;
import xyz.xfeatures.config.*;
import xyz.xfeatures.data.PlayerData;
import xyz.xfeatures.listener.*;

public final class XfeaturesRPGMoney extends JavaPlugin {

    public static XfeaturesRPGMoney instance;
    public static Economy economy = null;
    public MobConfig mobConfig;
    public FishConfig fishConfig;
    public BlockConfig blockConfig;
    public MessagesConfig messagesConfig;
    public MainConfig mainConfig;
    public PlayerData playerData;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault не найден! Плагин отключается.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        loadConfigs();

        playerData = new PlayerData(this);

        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFishListener(), this);
        getServer().getPluginManager().registerEvents(new SunflowerPickupListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        getCommand("rpgmoney").setExecutor(new RPGMoneyCommand(this));
    }
    
    private void loadConfigs() {
        mobConfig = new MobConfig(this);
        fishConfig = new FishConfig(this);
        blockConfig = new BlockConfig(this);
        messagesConfig = new MessagesConfig(this);
        mainConfig = new MainConfig(this);
    }
    
    public void reloadConfigs() {
        reloadConfig();
        loadConfigs();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onDisable() {
        if (playerData != null) {
            playerData.saveData();
        }
    }

    public NamespacedKey getNameKey() {
        return new NamespacedKey(this, "money");
    }
}