package me.shingaspt.sellwand.commands;

import me.shingaspt.sellwand.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GetSellWand extends BukkitCommand {

    private final MiniMessage mm = MiniMessage.miniMessage();

    public GetSellWand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Console is not allowed to run this command!");
            return true;
        }

        if (!(p.hasPermission("sellwand.admin")) || !(p.isOp())) {
            p.sendMessage(mm.deserialize("<red>Insufficient permission!"));
            return true;
        }

        FileConfiguration config = Plugin.getInstance().getConfig();

        int amount = args.length < 1 ? 1 : Integer.parseInt(args[0]);

        Material wandMat = Material.matchMaterial(config.getString("SellWand.wandItem.type"));
        ItemStack wand = new ItemStack(wandMat, amount);

        ItemMeta meta = wand.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(Plugin.getInstance(), "wand"), PersistentDataType.INTEGER, 1);
        meta.displayName(mm.deserialize("<italic:false>" + config.getString("SellWand.wandItem.name")));

        List<Component> lore = new ArrayList<>();
        for (String line : config.getStringList("SellWand.wandItem.lore")) {
            lore.add(mm.deserialize("<italic:false>" + line));
        }
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.values());

        wand.setItemMeta(meta);

        p.getInventory().addItem(wand);

        return true;
    }
}
