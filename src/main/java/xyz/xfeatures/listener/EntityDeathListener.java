package xyz.xfeatures.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;

import java.util.List;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) return;
        
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        
        LivingEntity entity = e.getEntity();
        
        List<Double> reward = XfeaturesRPGMoney.instance.mobConfig.getReward(entity.getType().name());
        if (reward == null || reward.size() < 2) return;
        
        if (entity.hasMetadata("spawned_by_spawner") && 
            XfeaturesRPGMoney.instance.mainConfig.getSpawnerMultiplier() < 1.0) {
            return;
        }
        
        double amount = MoneyUtil.getRandomInRange(reward.get(0), reward.get(1));
        
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        int lootingLevel = weapon.getEnchantmentLevel(Enchantment.LOOTING);
        
        if (lootingLevel > 0) {
            double multiplier = XfeaturesRPGMoney.instance.mainConfig.getLootingMultiplier(lootingLevel);
            amount *= multiplier;
            
            if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages()) {
                killer.sendActionBar("§e+" + String.format("%.0f", (multiplier - 1) * 100) + "% монет от Добычи " + lootingLevel);
            }
        }
        
        MoneyUtil.dropMoney(entity.getLocation(), amount);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;

        Economy eco = XfeaturesRPGMoney.economy;
        if (eco == null) return;
        double balance = eco.getBalance(victim);
        double drop = balance * 0.07;
        if (drop <= 0) return;
        eco.withdrawPlayer(victim, drop);
        MoneyUtil.dropMoney(victim.getLocation(), drop);
    }
}