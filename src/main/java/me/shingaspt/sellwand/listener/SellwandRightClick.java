package me.shingaspt.sellwand.listener;

import me.shingaspt.sellwand.Plugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SellwandRightClick implements Listener {

    @EventHandler
    public void onSell(PlayerInteractEvent e) {

        FileConfiguration config = Plugin.getInstance().getConfig();
        String mat = config.getString("SellWand.wandItem");
        assert mat != null;
        Material wand = Material.matchMaterial(mat);

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() == null) return;
        if (e.getItem() == null) return;
        if (e.getItem().getType() != wand) return;
        if (e.getClickedBlock().getType() != Material.CHEST) return;

        e.setCancelled(true);

        int totalPrice = 0;
        int soldAmount = 0;

        Chest chest = (Chest) e.getClickedBlock().getState();
        for (ItemStack item : chest.getInventory()) {
            if (item == null) continue;
            for (String price : config.getConfigurationSection("SellWand.items").getKeys(false)) {
                List<String> pricedItems = config.getStringList("SellWand.items." + price);
                if (pricedItems.contains(item.getType().toString())) {
                    totalPrice += Integer.parseInt(price);
                    soldAmount += item.getAmount();
                }
            }
        }

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(String.valueOf(totalPrice)));
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(String.valueOf(soldAmount)));
    }

}
