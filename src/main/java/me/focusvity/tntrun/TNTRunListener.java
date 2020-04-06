package me.focusvity.tntrun;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.List;

public class TNTRunListener implements Listener
{

    private List<String> jumped = new ArrayList<>();
    public static boolean started = false;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        final Player player = event.getPlayer();
        Block block = null;

        if (started)
        {
            // Up velocity - the highest it can jump.
            if (player.getVelocity().getY() == 0.0030162615090425808 && player.getLocation().getBlock().getType() == Material.AIR)
            {
                if (!jumped.contains(player.getName()))
                {
                    jumped.add(player.getName());
                }
            }

            // Down velocity
            if (player.getVelocity().getY() == -0.37663049823865513 && player.getLocation().getBlock().getType() == Material.AIR
                    && jumped.contains(player.getName()))
            {
                block = player.getLocation().subtract(0, 1, 0).getBlock(); // Landing block
                jumped.remove(player.getName());
            }

            // The block must be TNT in order to "ignite" it
            if (block != null && block.getType() == Material.TNT)
            {
                Entity tnt = block.getWorld().spawn(block.getLocation(), TNTPrimed.class);
                ((TNTPrimed) tnt).setFuseTicks(20);
                block.setType(Material.AIR);
            }
        }

        if (player.getGameMode() != GameMode.SPECTATOR && player.isFlying())
        {
            player.setFlying(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event)
    {
        // Stops people from flying
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosionPrime(ExplosionPrimeEvent event)
    {
        // No explosion to exist in this minigame
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (started)
        {
            final Player player = event.getEntity();
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
        }
    }
}
