package xyz.xfeatures.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Collection;

public class SunflowerPickupListener implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Item item = e.getItem();
        if (item.getItemStack().getType() != Material.SUNFLOWER) return;

        ItemMeta meta = item.getItemStack().getItemMeta();
        if (meta == null) return;

        Double amount = meta.getPersistentDataContainer()
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
            String message = XfeaturesRPGMoney.instance.messagesConfig.format("pickup", "amount", amount);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(message)
            );
        }

        playPickupSound(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!XfeaturesRPGMoney.instance.mainConfig.isFullInventoryCollect()) return;

        Player player = e.getPlayer();

        if (player.getInventory().firstEmpty() != -1) return;

        Collection<Item> nearbyItems = player.getWorld().getNearbyEntitiesByType(
                Item.class,
                player.getLocation(),
                1.5
        );

        for (Item item : nearbyItems) {
            if (item.getItemStack().getType() != Material.SUNFLOWER) continue;
            if (item.getPickupDelay() > 0) continue;

            ItemMeta meta = item.getItemStack().getItemMeta();
            if (meta == null) continue;

            Double amount = meta.getPersistentDataContainer()
                    .get(new NamespacedKey(XfeaturesRPGMoney.instance, "money"), PersistentDataType.DOUBLE);
            if (amount == null) continue;

            item.remove();

            Economy eco = XfeaturesRPGMoney.economy;
            if (eco != null) {
                eco.depositPlayer(player, amount);
                XfeaturesRPGMoney.instance.playerData.addCollectedMoney(player, amount);
            }

            if (XfeaturesRPGMoney.instance.mainConfig.isShowActionBarMessages()) {
                String message = XfeaturesRPGMoney.instance.messagesConfig.format("pickup", "amount", amount);
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent(message)
                );
            }

            playPickupSound(player);
        }
    }

    private void playPickupSound(Player player) {
        String soundName = XfeaturesRPGMoney.instance.mainConfig.getPickupSound();
        if (soundName == null || soundName.equalsIgnoreCase("none")) return;
        
        try {
            String normalizedSound = soundName.toLowerCase();

            if (soundName.matches("^[A-Z_]+$")) {
                normalizedSound = normalizedSound.replace("_", ".");
            }
            
            NamespacedKey soundKey;
            if (normalizedSound.contains(":")) {
                soundKey = NamespacedKey.fromString(normalizedSound);
            } else {
                soundKey = NamespacedKey.minecraft(normalizedSound);
            }
            
            if (soundKey == null) {
                XfeaturesRPGMoney.instance.getLogger().warning("Invalid sound name in config: " + soundName);
                return;
            }
            
            Sound sound = Registry.SOUNDS.get(soundKey);
            if (sound == null) {
                XfeaturesRPGMoney.instance.getLogger().warning("Sound not found in registry: " + soundName + " (tried: " + soundKey + ")");
                return;
            }
            
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (Exception e) {
            XfeaturesRPGMoney.instance.getLogger().warning("Error playing sound '" + soundName + "': " + e.getMessage());
        }
    }
}