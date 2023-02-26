package me.shingaspt.sellwand;

import me.shingaspt.sellwand.commands.GetSellWand;
import me.shingaspt.sellwand.listener.SellwandRightClick;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {

        instance = this;
        getServer().getPluginManager().registerEvents(new SellwandRightClick(), this);

        CommandMap map = Bukkit.getCommandMap();
        map.register("sellwand", "", new GetSellWand("sellwand"));

        saveDefaultConfig();
        getLogger().info("Sellwand has been loaded with no errors.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Sellwand has been unloaded with no errors.");
    }

    public static Plugin getInstance() { return instance; }

}
