package xyz.xfeatures.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.data.PlayerData.PlayerStats;
import xyz.xfeatures.util.CurrencyFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RPGMoneyCommand implements CommandExecutor, TabCompleter {
    private final XfeaturesRPGMoney plugin;

    public RPGMoneyCommand(XfeaturesRPGMoney plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("xfeatures.rpgmoney.reload")) {
                    sender.sendMessage(plugin.messagesConfig.format("no-permission"));
                    return true;
                }
                plugin.reloadConfigs();
                sender.sendMessage(plugin.messagesConfig.format("config-reloaded"));
                return true;
                
            case "stats":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.messagesConfig.format("player-only"));
                    return true;
                }
                
                Player player = (Player) sender;
                double collected = plugin.playerData.getCollectedMoney(player);
                
                sender.sendMessage(plugin.messagesConfig.format("stats-header"));
                sender.sendMessage(plugin.messagesConfig.format("stats-collected", "amount", collected));
                
                return true;
                
            case "top":
                if (!sender.hasPermission("xfeatures.rpgmoney.top")) {
                    sender.sendMessage(plugin.messagesConfig.format("no-permission"));
                    return true;
                }
                
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.messagesConfig.format("invalid-page-format"));
                        return true;
                    }
                }
                
                showTopPlayers(sender, page);
                return true;
                
            case "info":
                sender.sendMessage(plugin.messagesConfig.format("info-header"));
                sender.sendMessage(plugin.messagesConfig.format("info-version", "version", plugin.getDescription().getVersion()));

                String authorsMessage = plugin.messagesConfig.format("info-author", "authors", "XfeaturesGroup, kingnoype");
                sender.sendMessage(authorsMessage);
                
                sender.sendMessage(plugin.messagesConfig.format("info-fortune-header"));
                for (int i = 1; i <= 3; i++) {
                    double multiplier = plugin.mainConfig.getFortuneMultiplier(i);
                    sender.sendMessage(plugin.messagesConfig.format("info-fortune-entry", 
                            "level", i, 
                            "percent", String.format("%.0f", (multiplier - 1) * 100)));
                }
                
                sender.sendMessage(plugin.messagesConfig.format("info-looting-header"));
                for (int i = 1; i <= 3; i++) {
                    double multiplier = plugin.mainConfig.getLootingMultiplier(i);
                    sender.sendMessage(plugin.messagesConfig.format("info-looting-entry", 
                            "level", i, 
                            "percent", String.format("%.0f", (multiplier - 1) * 100)));
                }
                return true;
                
            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.messagesConfig.format("help-header"));
        sender.sendMessage(plugin.messagesConfig.format("help-reload"));
        sender.sendMessage(plugin.messagesConfig.format("help-stats"));
        sender.sendMessage(plugin.messagesConfig.format("help-top"));
        sender.sendMessage(plugin.messagesConfig.format("help-info"));
    }
    
    private void showTopPlayers(CommandSender sender, int page) {
        List<PlayerStats> topPlayers = plugin.playerData.getTopPlayers(10, (page - 1) * 10);
        
        if (topPlayers.isEmpty()) {
            sender.sendMessage(plugin.messagesConfig.format("no-data-for-page"));
            return;
        }
        
        sender.sendMessage(plugin.messagesConfig.format("top-header", "page", page));
        
        int startRank = (page - 1) * 10 + 1;
        for (int i = 0; i < topPlayers.size(); i++) {
            PlayerStats stats = topPlayers.get(i);
            sender.sendMessage(plugin.messagesConfig.format("top-player-entry", 
                    "rank", (startRank + i), 
                    "player", stats.getPlayerName(), 
                    "amount", stats.getMoney()));
        }
        
        sender.sendMessage(plugin.messagesConfig.format("top-next-page", "page", (page + 1)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            
            if (sender.hasPermission("xfeatures.rpgmoney.reload")) {
                completions.add("reload");
            }
            
            completions.add("stats");
            
            if (sender.hasPermission("xfeatures.rpgmoney.top")) {
                completions.add("top");
            }
            
            completions.add("info");
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("top")) {
            return List.of("1", "2", "3");
        }
        
        return new ArrayList<>();
    }
}