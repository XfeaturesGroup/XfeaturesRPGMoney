package xyz.xfeatures.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SunflowerPickupListener implements Listener {
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Item item = e.getItem();
        if (item.getItemStack().getType() != Material.SUNFLOWER) return;
        Double amount = item.getItemStack().getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(XfeaturesRPGMoney.instance, "money"), PersistentDataType.DOUBLE);
        if (amount == null) return;

        e.setCancelled(true);
        item.remove();
        Player player = (Player) e.getEntity();
        Economy eco = XfeaturesRPGMoney.economy;
        if (eco != null) {
            eco.depositPlayer(player, amount);

            XfeaturesRPGMoney.instance.playerData.addCollectedMoney(player, amount);
        }

        if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages()) {
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent("§eВы подняли " + String.format("%.2f", amount) + " монет!")
            );
        }
    }
}