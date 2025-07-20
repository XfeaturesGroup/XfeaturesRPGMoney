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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public final class MoneyUtil {

    private static final Random random = new Random();

    private MoneyUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void dropMoney(Location loc, double amount) {
        if (amount <= 0) return;

        if (random.nextDouble() > XfeaturesRPGMoney.instance.mainConfig.getDropChance()) {
            return;
        }

        amount = Math.min(amount, XfeaturesRPGMoney.instance.mainConfig.getMaxMoneyDrop());

        if (XfeaturesRPGMoney.instance.mainConfig.isCombineNearbyDrops()) {
            double radius = XfeaturesRPGMoney.instance.mainConfig.getCombineRadius();
            List<Item> nearbyItems = new ArrayList<>(loc.getWorld().getEntitiesByClass(Item.class));

            for (Item item : nearbyItems) {
                if (item.getLocation().distance(loc) <= radius && item.getItemStack().getType() == Material.SUNFLOWER) {
                    ItemMeta meta = item.getItemStack().getItemMeta();
                    if (meta != null) {
                        Double existingAmount = meta.getPersistentDataContainer().get(
                                XfeaturesRPGMoney.instance.getNameKey(),
                                PersistentDataType.DOUBLE
                        );

                        if (existingAmount != null) {
                            item.remove();
                            amount += existingAmount;
                        }
                    }
                }
            }
        }

        createAndDropMoneyItem(loc, amount);
    }

    private static void createAndDropMoneyItem(Location loc, double amount) {
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String displayName = XfeaturesRPGMoney.instance.messagesConfig.format("money-item-name", "amount", amount);

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

        String customName = XfeaturesRPGMoney.instance.messagesConfig.format("money-item-name", "amount", amount);

        net.kyori.adventure.text.Component customNameComponent =
                net.kyori.adventure.text.Component.text(customName);
        droppedItem.customName(customNameComponent);

        droppedItem.setCustomNameVisible(true);
        droppedItem.setPickupDelay(0);
    }

    public static double getRandomInRange(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static String formatMoney(double amount) {
        return String.format("%.2f", amount);
    }

    public static String formatMoneyWithCurrency(double amount) {
        return CurrencyFormatter.format(amount);
    }
}