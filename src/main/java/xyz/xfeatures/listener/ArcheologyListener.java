package xyz.xfeatures.listener;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.MoneyUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ArcheologyListener implements Listener {
    private final XfeaturesRPGMoney plugin;
    private final Random random = new Random();
    private final NamespacedKey archeologyKey;
    private final Set<Material> validBlocks = new HashSet<>(Arrays.asList(
            Material.SUSPICIOUS_SAND, Material.SUSPICIOUS_GRAVEL
    ));
    private static final int MARK_REMOVAL_DELAY = 1200;

    public ArcheologyListener(XfeaturesRPGMoney plugin) {
        this.plugin = plugin;
        this.archeologyKey = new NamespacedKey(plugin, "archeology_item");
    }

    @EventHandler
    public void onBrush(PlayerInteractEvent event) {
        if (!isValidBrushInteraction(event)) {
            return;
        }
        
        Block block = event.getClickedBlock();
        scheduleItemTracking(block);
    }

    private boolean isValidBrushInteraction(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BRUSH) {
            return false;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return false;
        }
        
        return validBlocks.contains(clickedBlock.getType());
    }

    private void scheduleItemTracking(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!validBlocks.contains(block.getType())) {
                    markNearbyItems(block);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 5L, 5L);
    }

    private void markNearbyItems(Block block) {
        block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 1, 1, 1).forEach(entity -> {
            if (entity instanceof Item) {
                Item droppedItem = (Item) entity;
                ItemStack itemStack = droppedItem.getItemStack();
                
                if (plugin.archeologyConfig.hasReward(itemStack.getType())) {
                    markItemAsArcheology(droppedItem);
                }
            }
        });
    }

    private void markItemAsArcheology(Item item) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta == null) {
            return;
        }
        
        meta.getPersistentDataContainer().set(archeologyKey, PersistentDataType.BYTE, (byte) 1);
        itemStack.setItemMeta(meta);
        item.setItemStack(itemStack);
        
        scheduleMarkRemoval(item);
    }

    private void scheduleMarkRemoval(Item item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (item.isDead() || !item.isValid()) {
                    return;
                }
                
                ItemStack currentItem = item.getItemStack();
                ItemMeta currentMeta = currentItem.getItemMeta();
                
                if (currentMeta != null && hasArcheologyMark(currentMeta)) {
                    currentMeta.getPersistentDataContainer().remove(archeologyKey);
                    currentItem.setItemMeta(currentMeta);
                    item.setItemStack(currentItem);
                }
            }
        }.runTaskLater(plugin, MARK_REMOVAL_DELAY);
    }

    private boolean hasArcheologyMark(ItemMeta meta) {
        return meta.getPersistentDataContainer().has(archeologyKey, PersistentDataType.BYTE);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        Item droppedItem = event.getItem();
        ItemStack item = droppedItem.getItemStack();
        Material material = item.getType();
        
        if (!plugin.archeologyConfig.hasReward(material)) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !hasArcheologyMark(meta)) {
            return;
        }

        meta.getPersistentDataContainer().remove(archeologyKey);
        item.setItemMeta(meta);

        processReward((Player) event.getEntity(), material);
    }
    
    private void processReward(Player player, Material material) {
        if (random.nextDouble() > plugin.mainConfig.getDropChance()) {
            return;
        }
        
        double[] reward = plugin.archeologyConfig.getReward(material);
        double amount = MoneyUtil.getRandomInRange(reward[0], reward[1]);
        
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            MoneyUtil.dropMoney(player.getLocation(), amount);
        });
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        Block block = event.getBlock();
        if (!validBlocks.contains(block.getType())) {
            return;
        }
        
        for (Item item : event.getItems()) {
            ItemStack itemStack = item.getItemStack();
            if (plugin.archeologyConfig.hasReward(itemStack.getType())) {
                markItemAsArcheology(item);
            }
        }
    }
}