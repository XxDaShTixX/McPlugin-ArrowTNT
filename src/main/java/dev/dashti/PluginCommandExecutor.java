package dev.dashti;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

public class PluginCommandExecutor implements CommandExecutor {

    private final Plugin plugin;
    private final PluginInfoModel pluginInfoModel;

    public PluginCommandExecutor(Plugin pl){
        plugin = pl;
        pluginInfoModel = new PluginInfoModel(plugin);
        pluginInfoModel.PluginObject = plugin; // Reference the plugin object
    }

    // onCommand listener
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // If the sender of the command it NOT a player
        if(!(commandSender instanceof Player)) { return false; }

        // Reference the player that invoked the command
        Player player = (Player) commandSender;

        // Command Listener
        if(command.getName().equalsIgnoreCase(pluginInfoModel.PluginCommand))
        {
            // [No permissions]
            if(!player.hasPermission(pluginInfoModel.PermissionUse)
                    && !player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                player.sendMessage(MessageFormat.format("[{0}] No permissions set.",
                    pluginInfoModel.PluginName
                ));
                return true;
            }

            // Command: ArrowTNT
            if(args.length == 0 && player.hasPermission(pluginInfoModel.PermissionUse)){
                // Get list of players with active status of this plugin
                List<String> activePlayers = PluginHelper.getInstance(plugin).GetListFromConfigFile("players.yml", "Active");

                // If player is listed
                if(activePlayers.contains(player.getName()))
                {
                    // Remove player
                    activePlayers.remove(player.getName());
                    PluginHelper.getInstance(plugin).SetListToConfig("players.yml","Active", activePlayers);
                    player.sendMessage(MessageFormat.format("[{0}] ArrowTNT ability turned off.",
                            pluginInfoModel.PluginName
                    ));
                } else {
                    // Add player
                    activePlayers.add(player.getName());
                    PluginHelper.getInstance(plugin).SetListToConfig("players.yml","Active", activePlayers);
                    player.sendMessage(MessageFormat.format("[{0}] ArrowTNT ability turned on.",
                            pluginInfoModel.PluginName
                    ));
                }
                return true;
            }

            // Command: ArrowTNT Reload
            if (args[0].equalsIgnoreCase("reload") && player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                plugin.reloadConfig();

                player.sendMessage(MessageFormat.format("[{0}] Config was successfully reloaded.",
                        pluginInfoModel.PluginName
                ));

                return true;
            }

            // Command: ArrowTNT setmult [amount]
            else if (args[0].equalsIgnoreCase("setmult") && player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                // If invalid arguments
                if(args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT setmult [amount]",
                            pluginInfoModel.PluginName
                    ));
                }
                else
                {
                    double newMulti;

                    //If input is double
                    try{
                        newMulti = Double.parseDouble(args[1]);
                        player.sendMessage(MessageFormat.format("[{0}] Set multiplier to {1}",
                                pluginInfoModel.PluginName,
                                newMulti
                        ));

                        // Add new value to config file
                        PluginHelper.getInstance(plugin).SetValueToConfig("config.yml", "ExplosionMultiplier", newMulti);
                    }catch(NumberFormatException e){
                        player.sendMessage(MessageFormat.format("[{0}] Something went wrong: {1}",
                                pluginInfoModel.PluginName,
                                e.getMessage()
                        ));
                        return true;
                    }
                }
                return true;
            }

            // Command: ArrowTNT setEnable [boolean]
            else if (args[0].equalsIgnoreCase("setEnable") && player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                boolean isEnabled;

                // if there was no argument
                if (args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT setenable [boolean]",
                            pluginInfoModel.PluginName
                    ));
                    return true;
                }
                else
                {
                    isEnabled = Boolean.parseBoolean(args[1]);
                    player.sendMessage(MessageFormat.format("[{0}] Set EnableArrowTNT to {1}",
                            pluginInfoModel.PluginName,
                            isEnabled
                    ));
                }
            }

            // Command: ArrowTNT AddWorld [WorldName]
            else if (args[0].equalsIgnoreCase("addworld") && player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                List<String> newWorldList;

                //if there was no argument
                if (args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT addworld [WorldName]",
                            pluginInfoModel.PluginName
                    ));
                    return true;
                }
                else //If there was an argument
                {
                    String newWorld = args[1];

                    //If new world-to-be-added is in the current world list
                    if(PluginHelper.getInstance(plugin).GetListFromConfigFile("config.yml", "AllowWorldList").contains(newWorld))
                    {
                        player.sendMessage(MessageFormat.format("[{0}] World ({1}) already in list",
                                pluginInfoModel.PluginName,
                                newWorld
                        ));
                    }
                    else //If world is not in list
                    {
                        newWorldList = PluginHelper.getInstance(plugin).GetListFromConfigFile("config.yml", "AllowWorldList");
                        newWorldList.add(newWorld);

                        PluginHelper.getInstance(plugin).SetListToConfig("config.yml", "AllowWorldList", newWorldList);

                        player.sendMessage(MessageFormat.format("[{0}] World successfully added",
                                pluginInfoModel.PluginName
                        ));
                    }
                }
            }

            // Command: ArrowTNT RemoveWorld [worldName]
            else if (args[0].equalsIgnoreCase("removeworld") && player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                List<String> newWorldList;

                //if there was no argument (true or false)
                if (args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT removeworld [WorldName]",
                            pluginInfoModel.PluginName
                    ));
                    return true;
                }
                else //If there was an argument
                {
                    String removeWorldName = args[1];

                    //If new world-to-be-removed is not in the list
                    if(!PluginHelper.getInstance(plugin).GetListFromConfigFile("config.yml", "AllowWorldList").contains(removeWorldName))
                    {
                        player.sendMessage(MessageFormat.format("[{0}] World ({1}) is not in list",
                                pluginInfoModel.PluginName,
                                removeWorldName
                        ));
                    }
                    else //If world is in list
                    {
                        newWorldList = PluginHelper.getInstance(plugin).GetListFromConfigFile("config.yml", "AllowWorldList");
                        newWorldList.remove(removeWorldName);

                        PluginHelper.getInstance(plugin).SetListToConfig("config.yml", "AllowWorldList", newWorldList);

                        player.sendMessage(MessageFormat.format("[{0}] World successfully removed",
                                pluginInfoModel.PluginName
                        ));
                    }
                }
            }

            // Command: ArrowTNT help
            else if (args[0].equalsIgnoreCase("help"))
            {
                player.sendMessage(MessageFormat.format("{0} {1} by {2}",
                        pluginInfoModel.PluginName,
                        pluginInfoModel.PluginVersion,
                        pluginInfoModel.AuthorName
                ));

                player.sendMessage(MessageFormat.format("/{0} : Toggle {1} ON / OFF",
                        pluginInfoModel.PluginCommand,
                        pluginInfoModel.PluginName
                ));

                player.sendMessage(MessageFormat.format("/{0} reload : Reload config.yml safely",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} setenable : Enable / disable plugin",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} setmult : Sets the explosion multiplier",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} addworld : Add world to allowed world list",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} removeworld : Remove world to allowed world list",
                        pluginInfoModel.PluginCommand
                ));

                return true;
            }
        }

        return true;
    }
}
