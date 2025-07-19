package xyz.xfeatures.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import xyz.xfeatures.XfeaturesRPGMoney;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (XfeaturesRPGMoney.instance.blockConfig.getReward(block.getType().name()) != null) {
            NamespacedKey key = new NamespacedKey(XfeaturesRPGMoney.instance, 
                    "player_placed_" + block.getX() + "_" + block.getY() + "_" + block.getZ());
            
            block.getChunk().getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        }
    }
}