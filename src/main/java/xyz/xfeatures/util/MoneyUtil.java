package xyz.xfeatures.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public final class MoneyUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private MoneyUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void dropMoney(Location loc, double amount) {
        if (RANDOM.nextDouble() > XfeaturesRPGMoney.instance.mainConfig.getDropChance()) {
            return;
        }

        amount = Math.min(amount, XfeaturesRPGMoney.instance.mainConfig.getMaxMoneyDrop());

        if (XfeaturesRPGMoney.instance.mainConfig.isCombineNearbyDrops()) {
            amount = combineWithNearbyMoney(loc, amount);
        }

        createAndDropMoneyItem(loc, amount);
    }

    private static double combineWithNearbyMoney(Location loc, double amount) {
        double radius = XfeaturesRPGMoney.instance.mainConfig.getCombineRadius();
        Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Item)) continue;

            Item droppedItem = (Item) entity;
            ItemStack stack = droppedItem.getItemStack();

            if (stack.getType() != Material.SUNFLOWER) continue;

            ItemMeta meta = stack.getItemMeta();
            if (meta == null) continue;

            PersistentDataContainer container = meta.getPersistentDataContainer();
            Double existingAmount = container.get(
                    XfeaturesRPGMoney.instance.getNameKey(),
                    PersistentDataType.DOUBLE
            );

            if (existingAmount != null) {
                amount += existingAmount;
                entity.remove();
                break;
            }
        }

        return amount;
    }

    private static void createAndDropMoneyItem(Location loc, double amount) {
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String displayName = "§e" + formatMoney(amount) + " монет";
            net.kyori.adventure.text.Component nameComponent =
                    net.kyori.adventure.text.Component.text(displayName);
            meta.displayName(nameComponent);

            meta.getPersistentDataContainer().set(
                    XfeaturesRPGMoney.instance.getNameKey(),
                    PersistentDataType.DOUBLE,
                    amount
            );
            item.setItemMeta(meta);
        }

        Item droppedItem = loc.getWorld().dropItemNaturally(loc, item);

        net.kyori.adventure.text.Component customNameComponent =
                net.kyori.adventure.text.Component.text("§e" + formatMoney(amount) + " монет");
        droppedItem.customName(customNameComponent);

        droppedItem.setCustomNameVisible(true);
        droppedItem.setPickupDelay(0);
    }

    public static double getRandomInRange(double min, double max) {
        return RANDOM.nextDouble(min, max);
    }

    public static String formatMoney(double amount) {
        return String.format("%.2f", amount);
    }
}