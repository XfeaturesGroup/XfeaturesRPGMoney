package xyz.xfeatures.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;

import java.util.List;

public class BlockBreakListener implements Listener {

    private final XfeaturesRPGMoney plugin = XfeaturesRPGMoney.instance;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        
        Block block = e.getBlock();
        
        if (block.getChunk().getPersistentDataContainer().has(
                plugin.getNameKey(),
                PersistentDataType.BOOLEAN)) {
            return;
        }
        
        List<Double> reward = plugin.blockConfig.getReward(block.getType().name());
        if (reward == null || reward.size() < 2) return;
        
        double amount = MoneyUtil.getRandomInRange(reward.get(0), reward.get(1));
        
        ItemStack tool = player.getInventory().getItemInMainHand();
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);
        if (fortuneLevel > 0) {
            handleFortuneMultiplier(player, amount, fortuneLevel);
            amount *= plugin.mainConfig.getFortuneMultiplier(fortuneLevel);
        }
        
        MoneyUtil.dropMoney(block.getLocation(), amount);
    }

    private void handleFortuneMultiplier(Player player, double baseAmount, int fortuneLevel) {
        double multiplier = plugin.mainConfig.getFortuneMultiplier(fortuneLevel);
        
        if (plugin.mainConfig.isShowActionBarMessages() && plugin.mainConfig.isShowFortuneMultiplierMessages() && fortuneLevel > 0) {
            String message = plugin.messagesConfig.get("fortune-multiplier")
                .replace("%level%", String.valueOf(fortuneLevel))
                .replace("%multiplier%", String.format("%.2f", multiplier));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }
}