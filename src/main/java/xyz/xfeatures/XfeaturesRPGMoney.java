package xyz.xfeatures;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
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

    private Metrics metrics;

    @Override
    public void onEnable() {
        try {
            int pluginId = 26636;
            metrics = new Metrics(this, pluginId);

            metrics.addCustomChart(new SingleLineChart("money_collected", () -> 100));

            instance = this;

            getLogger().info("Initialization XfeaturesRPGMoney v" + getDescription().getVersion());

            if (getServer().getPluginManager().getPlugin("Vault") == null) {
                getLogger().severe("Vault not found! Plugin disabled.");
                getLogger().severe("Please install Vault: https://www.spigotmc.org/resources/vault.34315/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (!setupEconomy()) {
                getLogger().severe("Unable to configure the economy! The plugin is being disabled.");
                getLogger().severe("Make sure you have a compatible economy plugin installed (e.g., Essentials, CMI).");
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

            setupCustomCharts();

            getLogger().info("bStats metrics enabled!");

            getLogger().info("XfeaturesRPGMoney successfully loaded!");
        } catch (Exception e) {
            getLogger().warning("Error during initialization bStats: " + e.getMessage());
        }
    }

    private void setupCustomCharts() {
        try {
            metrics.addCustomChart(new SingleLineChart("total_money", () -> {
                double totalMoney = 0;
                for (double money : playerData.getAllMoney()) {
                    totalMoney += money;
                }
                return (int) totalMoney;
            }));

            metrics.addCustomChart(new SimplePie("action_bar_messages", () ->
                mainConfig.isShowActionBarMessages() ? "Включено" : "Выключено"));

            metrics.addCustomChart(new SingleLineChart("players_with_money", () ->
                playerData.getPlayerCount()));

            metrics.addCustomChart(new SimplePie("fortune_multiplier_messages", () ->
                mainConfig.isShowFortuneMultiplierMessages() ? "Включено" : "Выключено"));

            metrics.addCustomChart(new SimplePie("looting_multiplier_messages", () ->
                mainConfig.isShowLootingMultiplierMessages() ? "Включено" : "Выключено"));
        } catch (Exception e) {
            getLogger().warning("Error when setting up graphs bStats: " + e.getMessage());
        }
    }

    private void loadConfigs() {
        mainConfig = new MainConfig(this);

        messagesConfig = new MessagesConfig(this);

        mobConfig = new MobConfig(this);
        fishConfig = new FishConfig(this);
        blockConfig = new BlockConfig(this);
        archeologyConfig = new ArcheologyConfig(this);
    }

    public void reloadConfigs() {
        reloadConfig();

        mainConfig.loadConfig();

        messagesConfig.reloadConfig();

        mobConfig = new MobConfig(this);
        fishConfig = new FishConfig(this);
        blockConfig = new BlockConfig(this);
        archeologyConfig = new ArcheologyConfig(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found! Make sure Vault is installed and loaded.");
            return false;
        }

        try {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                getLogger().severe("Unable to find the Vault economy provider. Make sure you have a compatible economy plugin installed (e.g., Essentials, CMI)");
                return false;
            }
            economy = rsp.getProvider();
            if (economy != null) {
                getLogger().info("Successfully connected to the economy through Vault: " + economy.getName());
                return true;
            } else {
                getLogger().severe("Unable to obtain the economy provider from Vault");
                return false;
            }
        } catch (Exception e) {
            getLogger().severe("An error occurred while configuring the economy: " + e.getMessage());
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