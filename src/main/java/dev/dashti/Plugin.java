package dev.dashti.mcpluginarrowtnt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Plugin extends JavaPlugin implements Listener {

    /**
     * Handles what heppens when plugin is enabled
     */
    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Log that plugin is enabled in console
        getLogger().info(PluginHelper.getInstance().pluginInfo.PluginName + " Enabled!");

        // If there was NO config file previously created, create one
        if(!this.getConfig().getName().equals("config.yml")) {
            this.getConfig().options().copyDefaults(true); //make copy from default
            this.saveConfig(); //Save config in plugins folder
        }
        PluginHelper.getInstance().pluginInfo.FileConfig = this.getConfig(); // Reference the configuration file
        PluginHelper.getInstance().pluginInfo.PluginObject = this; // Reference the plugin object

        // Register Command Executors
        org.bukkit.command.PluginCommand command = this.getCommand(PluginHelper.getInstance().pluginInfo.PluginCommand);
        if(command != null){ command.setExecutor(new PluginCommand()); }

        //Listeners
        registerEvents(this, new PluginListener()); //Register events
    }

    /**
     * Handles what happens when the plugin is disabled
     */
    @Override
    public void onDisable() {
        getLogger().info(PluginHelper.getInstance().pluginInfo.PluginName + " Disabled!");
    }

    /**
     * Handles event registrations for the plugin
     */
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners)
    {
        for (Listener listener : listeners)
        {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
