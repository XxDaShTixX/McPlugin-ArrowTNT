package dev.dashti;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class PluginListener implements Listener {

    private final Plugin plugin;
    private final PluginInfoModel pluginInfoModel;

    public PluginListener(Plugin pl){
        plugin = pl;
        pluginInfoModel = new PluginInfoModel(plugin);
        pluginInfoModel.PluginObject = plugin; // Reference the plugin object
    }

    /**
     * HAndles event of player joining server
     * @param e PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {

    }

    /**
     * Handles event of playing leaving server
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {

    }

    /**
     * Handles what happens when a projectile hits
     * @param event ProjectileHitEvent
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        // If plugin is enabled
        if (PluginHelper.getInstance(plugin).isEnabled ) {
            Entity entity = event.getEntity();
            float multiplier;

            // Set value if valid value
            try{
                multiplier = Float.parseFloat(
                        PluginHelper.getInstance(plugin).GetValueFromConfigFile("config.yml", "ExplosionMultiplier").toString()
                );
            }catch(NumberFormatException e){
                multiplier = 1;
            }

            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (arrow.getShooter() instanceof Player) {
                    Player shooter = (Player) arrow.getShooter();

                    try{
                        // Is player in a valid world?
                        boolean isInAllowedWorld = PluginHelper.getInstance(plugin).allowedWorlds.contains(shooter.getWorld().getName());
                        // Is player's arrowTNT active?
                        boolean isActive = PluginHelper.getInstance(plugin).playerList.contains(shooter.getName());

                        //If layer has perms + is in allowed world list
                        if (isInAllowedWorld && isActive)
                        {
                            shooter.getWorld().createExplosion(arrow.getLocation(), 5.0f * multiplier);
                        }
                    }catch (NullPointerException e){
                        plugin.getLogger().warning("Error: " + Arrays.toString(e.getStackTrace()));
                    }
                }
            }
        }
    }
}
