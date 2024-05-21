package dev.dashti.mcpluginarrowtnt;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.event.Listener;

public class PluginListener implements Listener {
    /**
     * HAndles event of player joining server
     * @param e PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        //if(!PluginHelper.getInstance().ArrowTNTUserList.containsKey(p)) //If player is not in HashMap
            PluginHelper.getInstance().ArrowTNTUserList.put(p, false); //Add him with default StaffChat set to OFF
    }

    /**
     * Handles event of playing leaving server
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player p = event.getPlayer();
        PluginHelper.getInstance().ArrowTNTUserList.remove(p);  //Remove player from map
    }

    /**
     * Handles what happens when a projectile hits
     * @param event ProjectileHitEvent
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (PluginHelper.getInstance().isEnabled) {
            Entity entity = event.getEntity();
            String temp = Double.toString(PluginHelper.getInstance().explosionMultiplier);
            float multiplier = 0;

            //Make sure it is a float
            if (PluginHelper.getInstance().isFloatParsable(temp))
                multiplier = Float.parseFloat(temp);
            else
                multiplier = 1;

            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (arrow.getShooter() instanceof Player) {
                    Player shooter = (Player) arrow.getShooter();

                    boolean allowedWorld = PluginHelper.getInstance().isPlayerInAllowedWorld(shooter);

                    //If layer has perms + is in allowed world list
                    if (allowedWorld &&
                            (shooter.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionUse)
                                    || shooter.hasPermission(PluginHelper.getInstance().pluginInfo.PermissionAdmin)
                    ))
                        if (PluginHelper.getInstance().ArrowTNTUserList.containsKey(shooter))
                            if (PluginHelper.getInstance().ArrowTNTUserList.get(shooter))
                                shooter.getWorld().createExplosion(arrow.getLocation(), 5.0f * multiplier);
                }
            }
        }
    }
}
