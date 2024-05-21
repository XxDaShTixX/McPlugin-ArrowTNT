package dev.dashti.mcpluginarrowtnt;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PluginHelper {

    // Variables
    public PluginInfo pluginInfo = new PluginInfo(); // Plugin info
    public boolean isEnabled = pluginInfo.FileConfig.getBoolean("EnableArrowTNT");
    public double explosionMultiplier = pluginInfo.FileConfig.getDouble("ExplosionMultiplier");

    public List<String> worldList = pluginInfo.FileConfig.getStringList("AllowWorldList");

    // Creating the HashMap which stores the player and the ArrowTNT toggle boolean
    public HashMap<Player, Boolean> ArrowTNTUserList = new HashMap<>();

    // Singleton instance
    private static PluginHelper instance = null;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private PluginHelper()
    {
    }

    // Static method to create instance of Singleton class
    public static synchronized PluginHelper getInstance()
    {
        if (instance == null)
            instance = new PluginHelper();

        return instance;
    }

    /**
     * Check if string can be parsed into a number
     * @param player player to check status of
     * @return true if player is allowed in the world
     */
    public boolean isPlayerInAllowedWorld(Player player)
    {
        boolean allowed = false;

        for(String world : worldList)
        {
            //If player is in world that is in allowedWorld list
            if(player.getWorld().getName().equalsIgnoreCase(world))
            {
                allowed = true;
            }
        }

        return allowed;
    }

    /**
     * Check if string can be parsed into a number
     * @param input the string to check
     * @return true if it is an int
     */
    public boolean isIntParsable(String input)
    {
        boolean parsable = true;

        try
        {
            Integer.parseInt(input);
        }
        catch(NumberFormatException e)
        {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Check if string can be parsed into a number
     * @param input the string to check
     * @return true if a float
     */
    public boolean isFloatParsable(String input)
    {
        boolean parsable = true;

        try
        {
            Float.parseFloat(input);
        }
        catch(NumberFormatException e)
        {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Check if string can be parsed into a number
     * @param input the string to check
     * @return true if double
     */
    public boolean isDoubleParsable(String input)
    {
        boolean parsable = true;

        try
        {
            Double.parseDouble(input);
        }
        catch(NumberFormatException e)
        {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Check if string can be parsed into a boolean
     * @param input the string to check
     * @return true if boolean
     */
    public boolean isBoolParsable(String input) {
        return "true".equalsIgnoreCase(input) || "false".equalsIgnoreCase(input);
    }

}
