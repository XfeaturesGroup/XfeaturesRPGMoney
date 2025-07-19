package xyz.xfeatures.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;

import java.util.List;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();

        List<Double> reward = XfeaturesRPGMoney.instance.blockConfig.getReward(block.getType().name());
        if (reward == null || reward.size() < 2) return;

        NamespacedKey key = new NamespacedKey(XfeaturesRPGMoney.instance, 
                "player_placed_" + block.getX() + "_" + block.getY() + "_" + block.getZ());
        
        Boolean isPlayerPlaced = block.getChunk().getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN);

        if (isPlayerPlaced != null && isPlayerPlaced) {
            block.getChunk().getPersistentDataContainer().remove(key);
            return;
        }

        double amount = MoneyUtil.getRandomInRange(reward.get(0), reward.get(1));

        ItemStack tool = player.getInventory().getItemInMainHand();
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);
        
        if (fortuneLevel > 0) {
            double multiplier = XfeaturesRPGMoney.instance.mainConfig.getFortuneMultiplier(fortuneLevel);
            amount *= multiplier;

            if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages()) {
                player.sendActionBar("§e+" + String.format("%.0f", (multiplier - 1) * 100) + "% монет от Удачи " + fortuneLevel);
            }
        }
        
        MoneyUtil.dropMoney(block.getLocation(), amount);

        block.getChunk().getPersistentDataContainer().remove(key);
    }
}