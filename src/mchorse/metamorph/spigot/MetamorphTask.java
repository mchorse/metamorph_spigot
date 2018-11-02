package mchorse.metamorph.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Metamorph task
 * 
 * This bad boy is responsible for constantly track other players and 
 * send morphs whenever other players need it.
 */
public class MetamorphTask extends BukkitRunnable
{
    public static final MetamorphTask task = new MetamorphTask();

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            MetamorphListener.morphs.get(player.getUniqueId()).updateNearbyPlayers(player, false);
        }
    }
}