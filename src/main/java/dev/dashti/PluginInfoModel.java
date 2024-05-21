package dev.dashti;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginInfoModel {
    JavaPlugin PluginObject = null;

    String PluginName = "ArrowTNT"; // Plugin name
    String PluginCommand = "ArrowTNT"; // Command you enter to interact with plugin
    String PluginVersion = "v1.5";
    String AuthorName = "Ali Dashti";
    String AuthorEmail = "contact@dashti.dev";
    String AuthorWebsite = "https://dashti.dev";
    String PermissionUse = "ArrowTNT.Use";
    String PermissionAdmin = "ArrowTNT.Admin";

    String[] PluginDependency = new String[]{""}; // Other plugins this plugin relies on

    PluginInfoModel(Plugin pl){
        PluginObject = pl;
        //FileConfig = pl.getConfig();
    }
}
