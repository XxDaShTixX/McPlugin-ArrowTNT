package dev.dashti;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

public class PluginCommandExecutor implements CommandExecutor {

    private Plugin plugin;

    PluginCommandExecutor(Plugin pl){
        plugin = pl;
        PluginCommand x = plugin.getCommand("piggybanks");
    }

    // onCommand listener
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // Reference the player that invoked the command
        Player player = (Player) commandSender;

        // If player was not initialized in the list, add player
        if(!PluginHelper.getInstance().ArrowTNTUserList.containsKey(player)) //If player is not in list
            PluginHelper.getInstance().ArrowTNTUserList.put(player, false); //Add him with default list set to OFF

        // Command Listener
        if(command.getName().equalsIgnoreCase(PluginHelper.getInstance().pluginInfo.PluginCommand))
        {
            // [No permissions]
            if(!player.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionUse)
                    && !player.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionAdmin))
            {
                player.sendMessage(MessageFormat.format("[{0}] No permissions set.",
                        PluginHelper.getInstance().pluginInfo.PluginName
                ));
                return true;
            }

            // [Use or Admin permissions]
            if(player.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionUse)
                    || player.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionAdmin))
            {
                // Command: ArrowTNT
                if (args.length == 0)
                {
                    // If the value of the player in the list is true (meaning it is enabled)
                    if(PluginHelper.getInstance().ArrowTNTUserList.get(player))
                    {
                        // Disable usage
                        PluginHelper.getInstance().ArrowTNTUserList.replace(player, true, false);
                        player.sendMessage(MessageFormat.format("[{0}] ArrowTNT ability turned off.",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                        return true;
                    } else
                    {
                        PluginHelper.getInstance().ArrowTNTUserList.replace(player, false, true);
                        player.sendMessage(MessageFormat.format("[{0}] ArrowTNT ability turned on.",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                        return true;
                    }
                }

                return true;
            }

            // [Admin permissions only]
            if(player.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionAdmin))
            {
                // Command: ArrowTNT Reload
                if (args[0].equalsIgnoreCase("reload"))
                {
                    PluginHelper.getInstance().isEnabled = PluginHelper.getInstance().pluginInfo.FileConfig.getBoolean("EnableArrowTNT");
                    PluginHelper.getInstance().explosionMultiplier = PluginHelper.getInstance().pluginInfo.FileConfig.getDouble("ExplosionMultiplier");
                    PluginHelper.getInstance().pluginInfo.PluginObject.reloadConfig();

                    player.sendMessage(MessageFormat.format("[{0}] Config was successfully reloaded.",
                            PluginHelper.getInstance().pluginInfo.PluginName
                    ));

                    return true;
                }

                // Command: ArrowTNT setmult [amount]
                if (args[0].equalsIgnoreCase("setmult"))
                {
                    // If invalid arguments
                    if(args.length != 2)
                    {
                        player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT setmult [amount]",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                    }
                    else
                    {
                        double newMult = 0;

                        //If input is double
                        if(PluginHelper.getInstance().isDoubleParsable(args[1]))
                            newMult = Double.parseDouble(args[1]);
                        else //If not, do not update
                        {
                            player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT setmult [amount]",
                                    PluginHelper.getInstance().pluginInfo.PluginName)
                            );
                            return true;
                        }

                        PluginHelper.getInstance().pluginInfo.FileConfig.set("ExplosionMultiplier", newMult);
                        PluginHelper.getInstance().pluginInfo.PluginObject.saveConfig();

                        player.sendMessage(MessageFormat.format("[{0}] The explosion multiplier was successfully changed to {1}.",
                                PluginHelper.getInstance().pluginInfo.PluginName,
                                newMult
                        ));
                    }
                    return true;
                }

                // Command: ArrowTNT setEnable [boolean]
                if (args[0].equalsIgnoreCase("setEnable"))
                {
                    boolean isEnabled = PluginHelper.getInstance().pluginInfo.FileConfig.getBoolean("EnableArrowTNT");

                    //if there was no argument (true or false)
                    if (args.length != 2)
                    {
                        player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT setenable [boolean]",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                        return true;
                    }
                    else //If there was an argument
                    {
                        //If the argument is a boolean
                        if(PluginHelper.getInstance().isBoolParsable(args[1]))
                        {
                            //if user input is true and config already true
                            if(Boolean.parseBoolean(args[1]) == isEnabled)
                            {
                                player.sendMessage(MessageFormat.format("[{0}] Plugin already set enabled.",
                                        PluginHelper.getInstance().pluginInfo.PluginName
                                ));
                                return true;
                            }
                            else
                            {
                                PluginHelper.getInstance().pluginInfo.FileConfig.set("EnableArrowTNT", Boolean.parseBoolean(args[1]));
                                PluginHelper.getInstance().pluginInfo.PluginObject.saveConfig();
                                return true;
                            }
                        }
                    }
                }

                // Command: ArrowTNT AddWorld [WorldName]
                if (args[0].equalsIgnoreCase("addworld"))
                {
                    List<String> newWorldList;

                    //if there was no argument (true or false)
                    if (args.length != 2)
                    {
                        player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT addworld [WorldName]",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                        return true;
                    }
                    else //If there was an argument
                    {
                        String newWorld = args[1];

                        //If new world-to-be-added is in the current world list
                        if(PluginHelper.getInstance().worldList.contains(newWorld))
                        {
                            player.sendMessage(MessageFormat.format("[{0}] World already in list",
                                    PluginHelper.getInstance().pluginInfo.PluginName
                            ));
                        }
                        else //If world is not in list
                        {
                            newWorldList = PluginHelper.getInstance().worldList;
                            newWorldList.add(newWorld);

                            PluginHelper.getInstance().pluginInfo.FileConfig.set("AllowWorldList", newWorldList);
                            PluginHelper.getInstance().pluginInfo.PluginObject.saveConfig();

                            player.sendMessage(MessageFormat.format("[{0}] World successfully added",
                                    PluginHelper.getInstance().pluginInfo.PluginName
                            ));
                        }
                    }
                }

                // Command: ArrowTNT RemoveWorld [worldName]
                if (args[0].equalsIgnoreCase("removeworld"))
                {
                    List<String> newWorldList;

                    //if there was no argument (true or false)
                    if (args.length != 2)
                    {
                        player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT removeworld [WorldName]",
                                PluginHelper.getInstance().pluginInfo.PluginName
                        ));
                        return true;
                    }
                    else //If there was an argument
                    {
                        String removeWorldName = args[1];

                        //If new world-to-be-removed is not in the list
                        if(!PluginHelper.getInstance().worldList.contains(removeWorldName))
                        {
                            player.sendMessage(MessageFormat.format("[{0}] World not found in list",
                                    PluginHelper.getInstance().pluginInfo.PluginName
                            ));
                        }
                        else //If world is in list
                        {
                            newWorldList = PluginHelper.getInstance().worldList;
                            newWorldList.remove(removeWorldName);

                            PluginHelper.getInstance().pluginInfo.FileConfig.set("AllowWorldList", newWorldList);
                            PluginHelper.getInstance().pluginInfo.PluginObject.saveConfig();

                            player.sendMessage(MessageFormat.format("[{0}] World successfully removed",
                                    PluginHelper.getInstance().pluginInfo.PluginName
                            ));
                        }
                    }
                }

                // Command: ArrowTNT help
                if (args[0].equalsIgnoreCase("help"))
                {
                    player.sendMessage(MessageFormat.format("{0} {1}",
                            PluginHelper.getInstance().pluginInfo.PluginName,
                            PluginHelper.getInstance().pluginInfo.PluginVersion
                    ));

                    player.sendMessage(MessageFormat.format("/{0} : Toggle {1} ON / OFF",
                            PluginHelper.getInstance().pluginInfo.PluginCommand,
                            PluginHelper.getInstance().pluginInfo.PluginName
                    ));

                    player.sendMessage(MessageFormat.format("/{0} reload : Reload config.yml safely",
                            PluginHelper.getInstance().pluginInfo.PluginCommand
                    ));

                    player.sendMessage(MessageFormat.format("/{0} setenable : Enable / disable plugin",
                            PluginHelper.getInstance().pluginInfo.PluginCommand
                    ));

                    player.sendMessage(MessageFormat.format("/{0} setmult : Sets the explosion multiplier",
                            PluginHelper.getInstance().pluginInfo.PluginCommand
                    ));

                    player.sendMessage(MessageFormat.format("/{0} addworld : Add world to allowed world list",
                            PluginHelper.getInstance().pluginInfo.PluginCommand
                    ));

                    player.sendMessage(MessageFormat.format("/{0} removeworld : Remove world to allowed world list",
                            PluginHelper.getInstance().pluginInfo.PluginCommand
                    ));

                    return true;
                }

                return true;
            }
        }

        return true;
    }


}
