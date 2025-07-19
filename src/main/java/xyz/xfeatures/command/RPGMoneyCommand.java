package xyz.xfeatures.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.data.PlayerData.PlayerStats;

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
                    sender.sendMessage(ChatColor.RED + "У вас нет прав для выполнения этой команды!");
                    return true;
                }
                plugin.reloadConfigs();
                sender.sendMessage(ChatColor.RED + "Конфигурация перезагружена!");
                return true;
                
            case "stats":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Эта команда доступна только для игроков!");
                    return true;
                }
                
                Player player = (Player) sender;
                double collected = plugin.playerData.getCollectedMoney(player);
                
                sender.sendMessage(ChatColor.RED + "=== Ваша статистика ===");
                sender.sendMessage(ChatColor.GRAY + "Всего собрано монет: " + ChatColor.RED + String.format("%.2f", collected));
                
                return true;
                
            case "top":
                if (!sender.hasPermission("xfeatures.rpgmoney.top")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав для выполнения этой команды!");
                    return true;
                }
                
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Неверный формат страницы!");
                        return true;
                    }
                }
                
                showTopPlayers(sender, page);
                return true;
                
            case "info":
                sender.sendMessage(ChatColor.RED + "=== XfeaturesRPGMoney ===");
                sender.sendMessage(ChatColor.GRAY + "Версия: " + plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Автор: XfeaturesGroup, kingnoype");
                
                sender.sendMessage(ChatColor.RED + "Множители удачи:");
                for (int i = 1; i <= 3; i++) {
                    double multiplier = plugin.mainConfig.getFortuneMultiplier(i);
                    sender.sendMessage(ChatColor.GRAY + "  Удача " + i + ": +" + 
                            String.format("%.0f", (multiplier - 1) * 100) + "%");
                }
                
                sender.sendMessage(ChatColor.RED + "Множители добычи:");
                for (int i = 1; i <= 3; i++) {
                    double multiplier = plugin.mainConfig.getLootingMultiplier(i);
                    sender.sendMessage(ChatColor.GRAY + "  Добыча " + i + ": +" + 
                            String.format("%.0f", (multiplier - 1) * 100) + "%");
                }
                return true;
                
            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "=== XfeaturesRPGMoney ===");
        sender.sendMessage(ChatColor.GRAY + "/rpgmoney reload - Перезагрузить конфигурацию");
        sender.sendMessage(ChatColor.GRAY + "/rpgmoney stats - Показать вашу статистику");
        sender.sendMessage(ChatColor.GRAY + "/rpgmoney top [страница] - Показать топ игроков");
        sender.sendMessage(ChatColor.GRAY + "/rpgmoney info - Информация о плагине");
    }
    
    private void showTopPlayers(CommandSender sender, int page) {
        List<PlayerStats> topPlayers = plugin.playerData.getTopPlayers(10, (page - 1) * 10);
        
        if (topPlayers.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Нет данных для отображения на этой странице!");
            return;
        }
        
        sender.sendMessage(ChatColor.RED + "=== Топ игроков по собранным монетам (Страница " + page + ") ===");
        
        int startRank = (page - 1) * 10 + 1;
        for (int i = 0; i < topPlayers.size(); i++) {
            PlayerStats stats = topPlayers.get(i);
            sender.sendMessage(ChatColor.GRAY + "#" + (startRank + i) + " " + 
                    stats.getPlayerName() + ": " + ChatColor.RED + 
                    String.format("%.2f", stats.getMoney()) + " монет");
        }
        
        sender.sendMessage(ChatColor.GRAY + "Используйте /rpgmoney top " + (page + 1) + " для просмотра следующей страницы");
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