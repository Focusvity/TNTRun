package me.focusvity.tntrun;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game
{

    private BukkitTask game = null;
    private final List<PotionEffectType> EFFECTS = Arrays.asList(PotionEffectType.SPEED,
            PotionEffectType.BLINDNESS,
            PotionEffectType.JUMP,
            PotionEffectType.INVISIBILITY);

    public void init()
    {
        TNTRunListener.started = true;
        game = new BukkitRunnable()
        {
            int count = TNTRun.plugin.getConfig().getInt("game_time") * 60;
            int seconds = TNTRun.plugin.getConfig().getInt("seconds");
            int defSeconds = seconds;
            boolean shownTime = false;

            @Override
            public void run()
            {
                if (TNTRunListener.started == false)
                {
                    Bukkit.getScheduler().cancelAllTasks();
                }

                if (!shownTime)
                {
                    sendTitle((count / 60) + " minutes remaining", ChatColor.GREEN, 30);
                    shownTime = true;
                }

                if ((count / 60) == 5)
                {
                    sendTitle("5 minutes remaining!", ChatColor.GREEN, 30);
                }
                else if (count == 60)
                {
                    sendTitle("1 minute remaining!", ChatColor.YELLOW, 30);
                }
                else if (count == 30)
                {
                    sendTitle("30 seconds remaining!", ChatColor.YELLOW, 30);
                }
                else if (count == 3 || count == 2 || count == 1)
                {
                    sendTitle(count + " seconds left!", ChatColor.YELLOW, 30);
                }
                else if (count == 0)
                {
                    sendTitle("Time is up!", ChatColor.RED, 30);
                    TNTRunListener.started = false;
                }
                count--;

                if (seconds > 0)
                {
                    seconds--;
                }
                else
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        if (player.getGameMode() != GameMode.SPECTATOR)
                        {
                            Random random = new Random();
                            int pick = random.nextInt(EFFECTS.size());
                            player.addPotionEffect(EFFECTS.get(pick).createEffect(200, 5));
                        }
                    }
                    seconds = defSeconds;
                }
            }
        }.runTaskTimer(TNTRun.plugin, 0, 20);
    }

    private void sendTitle(String message, ChatColor color, int time)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\""
            + ",\"color\": " + color.name().toLowerCase() + "}");
            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            PacketPlayOutTitle length = new PacketPlayOutTitle(5, time, 5);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
        }
    }
}
