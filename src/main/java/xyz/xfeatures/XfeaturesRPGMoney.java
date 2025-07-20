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
    public ArcheologyConfig archeologyConfig;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Инициализация XfeaturesRPGMoney v" + getDescription().getVersion());

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault не найден! Плагин отключается.");
            getLogger().severe("Пожалуйста, установите Vault: https://www.spigotmc.org/resources/vault.34315/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy()) {
            getLogger().severe("Не удалось настроить экономику! Плагин отключается.");
            getLogger().severe("Убедитесь, что у вас установлен совместимый плагин экономики (например, Essentials, CMI)");
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
        getServer().getPluginManager().registerEvents(new ArcheologyListener(this), this);

        getCommand("rpgmoney").setExecutor(new RPGMoneyCommand(this));
    
        getLogger().info("XfeaturesRPGMoney успешно загружен!");
    }
    
    private void loadConfigs() {
        mobConfig = new MobConfig(this);
        fishConfig = new FishConfig(this);
        blockConfig = new BlockConfig(this);
        messagesConfig = new MessagesConfig(this);
        mainConfig = new MainConfig(this);
        archeologyConfig = new ArcheologyConfig(this);
    }
    
    public void reloadConfigs() {
        reloadConfig();
        mobConfig = new MobConfig(this);
        fishConfig = new FishConfig(this);
        blockConfig = new BlockConfig(this);
        messagesConfig = new MessagesConfig(this);
        mainConfig.loadConfig();
        archeologyConfig = new ArcheologyConfig(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault плагин не найден! Убедитесь, что Vault установлен и загружен.");
            return false;
        }
        
        try {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                getLogger().severe("Не удалось найти экономический провайдер Vault. Убедитесь, что установлен совместимый плагин экономики (например, Essentials, CMI).");
                return false;
            }
            economy = rsp.getProvider();
            if (economy != null) {
                getLogger().info("Успешно подключен к экономике через Vault: " + economy.getName());
                return true;
            } else {
                getLogger().severe("Не удалось получить провайдер экономики от Vault.");
                return false;
            }
        } catch (Exception e) {
            getLogger().severe("Произошла ошибка при настройке экономики: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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