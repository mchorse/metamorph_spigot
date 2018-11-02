package mchorse.metamorph.spigot;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Morph player
 * 
 * This bad boy is responsible for keeping track of nearby players, and 
 * also sending morphs whenever needed. 
 */
public class MorphPlayer
{
    /**
     * Map of nearby players 
     */
    public Map<UUID, Player> nearbyPlayers = new HashMap<UUID, Player>();

    /**
     * Current morph
     */
    public String morph = "";

    public void updateNearbyPlayers(Player player, boolean later)
    {
        /* I haven't found a way to get player's tracking range via 
         * Spigot's API, so we'll have to resort to hardcoded default 
         * value */
        final double limit = 48.0 * 48.0;

        for (Player otherPlayer : Bukkit.getOnlinePlayers())
        {
            if (player == otherPlayer || player.getWorld() != otherPlayer.getWorld())
            {
                continue;
            }

            UUID id = otherPlayer.getUniqueId();
            Player reference = this.nearbyPlayers.get(id);
            double distance = player.getLocation().distanceSquared(otherPlayer.getLocation());

            if (reference == null && distance < limit)
            {
                this.nearbyPlayers.put(id, otherPlayer);

                /* Later is true only in the listener class */
                if (later) this.sendMorphLater(otherPlayer);
                else this.sendMorph(otherPlayer);
            }
            else if (reference != null && distance > limit)
            {
                this.nearbyPlayers.remove(id);
            }
        }
    }

    public void sendMorph(Player otherPlayer)
    {
        if (!this.morph.isEmpty())
        {
            otherPlayer.sendPluginMessage(MetamorphPlugin.instance, "Metamorph", this.morph.getBytes(Charset.forName("UTF-8")));
        }
    }

    /**
     * Same as send morph, but with a 5 tick delay. Used for log in 
     * listener. Looks like clients can't instantly process custom 
     * payload packets, so we have to delay it. 
     */
    public void sendMorphLater(Player otherPlayer)
    {
        Bukkit.getScheduler().runTaskLater(MetamorphPlugin.instance, new Runnable()
        {
            @Override
            public void run()
            {
                MorphPlayer.this.sendMorph(otherPlayer);
            }
        }, 5L);
    }
}