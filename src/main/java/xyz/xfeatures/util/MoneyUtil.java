package xyz.xfeatures.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;

import java.util.Collection;
import java.util.Random;

public class MoneyUtil {
    private static final Random random = new Random();

    public static double getRandomInRange(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static void dropMoney(Location loc, double amount) {
        if (random.nextDouble() > XfeaturesRPGMoney.instance.mainConfig.getDropChance()) {
            return;
        }
        
        double maxDrop = XfeaturesRPGMoney.instance.mainConfig.getMaxMoneyDrop();
        if (amount > maxDrop) {
            amount = maxDrop;
        }
        
        if (XfeaturesRPGMoney.instance.mainConfig.isCombineNearbyDrops()) {
            double radius = XfeaturesRPGMoney.instance.mainConfig.getCombineRadius();
            Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
            
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    if (item.getItemStack().getType() == Material.SUNFLOWER) {
                        ItemMeta meta = item.getItemStack().getItemMeta();
                        if (meta != null) {
                            Double existingAmount = meta.getPersistentDataContainer()
                                    .get(XfeaturesRPGMoney.instance.getNameKey(), PersistentDataType.DOUBLE);
                            
                            if (existingAmount != null) {
                                amount += existingAmount;
                                item.remove();
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e" + String.format("%.2f", amount) + " монет");
            meta.getPersistentDataContainer().set(
                    XfeaturesRPGMoney.instance.getNameKey(),
                    PersistentDataType.DOUBLE,
                    amount
            );
            item.setItemMeta(meta);
        }
        
        Item droppedItem = loc.getWorld().dropItemNaturally(loc, item);
        droppedItem.setCustomName("§e" + String.format("%.2f", amount) + " монет");
        droppedItem.setCustomNameVisible(true);
        droppedItem.setPickupDelay(0);
    }
}