package xyz.xfeatures.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.CurrencyFormatter;
import xyz.xfeatures.util.MoneyUtil;
import java.util.List;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) return;
        
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        
        LivingEntity entity = e.getEntity();
        
        double multiplier = 1.0;
        if (entity.hasMetadata("spawner")) {
            multiplier = XfeaturesRPGMoney.instance.mainConfig.getSpawnerMultiplier();
        }
        
        List<Double> reward = XfeaturesRPGMoney.instance.mobConfig.getReward(entity.getType().name());
        if (reward == null || reward.size() < 2) return;
        
        double amount = MoneyUtil.getRandomInRange(reward.get(0), reward.get(1)) * multiplier;
        
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        int lootingLevel = weapon.getEnchantmentLevel(Enchantment.LOOTING);
        if (lootingLevel > 0) {
            double lootingMultiplier = XfeaturesRPGMoney.instance.mainConfig.getLootingMultiplier(lootingLevel);
            amount *= lootingMultiplier;
            
            if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages() && 
                XfeaturesRPGMoney.instance.mainConfig.isShowLootingMultiplierMessages()) {
                String message = XfeaturesRPGMoney.instance.messagesConfig.get("looting-multiplier")
                    .replace("%level%", String.valueOf(lootingLevel))
                    .replace("%multiplier%", String.format("%.2f", lootingMultiplier));
                killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            }
        }
        
        MoneyUtil.dropMoney(entity.getLocation(), amount);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        double balance = XfeaturesRPGMoney.economy.getBalance(player);
        
        if (balance <= 0) return;
        
        double dropPercentage = XfeaturesRPGMoney.instance.mainConfig.getPlayerDeathDropPercentage();
        double amountToDrop = balance * dropPercentage;
        
        if (amountToDrop <= 0) return;
        
        XfeaturesRPGMoney.economy.withdrawPlayer(player, amountToDrop);
        MoneyUtil.dropMoney(player.getLocation(), amountToDrop);
        
        if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages()) {
            String message = XfeaturesRPGMoney.instance.messagesConfig.get("death-money-drop");
            message = CurrencyFormatter.replaceAmount(message, amountToDrop);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }
}