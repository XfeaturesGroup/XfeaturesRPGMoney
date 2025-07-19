package xyz.xfeatures.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;
import java.util.List;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerFishListener implements Listener {
    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        if (e.getCaught() == null || !(e.getCaught() instanceof org.bukkit.entity.Item)) return;
        ItemStack item = ((org.bukkit.entity.Item) e.getCaught()).getItemStack();

        List<Double> reward = XfeaturesRPGMoney.instance.fishConfig.getReward(item.getType().name());
        if (reward == null || reward.size() < 2) return;

        double amount = MoneyUtil.getRandomInRange(reward.get(0), reward.get(1));
        Player player = e.getPlayer();
        Economy eco = XfeaturesRPGMoney.economy;
        if (eco != null) {
            eco.depositPlayer(player, amount);
        }
        String msg = XfeaturesRPGMoney.instance.messagesConfig.get("pickup")
                .replace("%amount%", String.format("%.2f", amount));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }
}