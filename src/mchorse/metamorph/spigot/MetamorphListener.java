package mchorse.metamorph.spigot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Metamorph plugin listener
 * 
 * Because Spigot's retarded system doesn't have an entity tracker, we 
 * have to resort to this dumb ass fucking system which has to manually 
 * track the players.
 */
public class MetamorphListener implements Listener
{
    public static Map<UUID, MorphPlayer> morphs = new HashMap<UUID, MorphPlayer>();

    @EventHandler
    public void onPlayerLogsIn(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        MorphPlayer morph = morphs.get(id);

        if (morph == null)
        {
            morph = new MorphPlayer();
            morph.morph = player.getName();
            morphs.put(id, morph);
        }

        morph.updateNearbyPlayers(player, true);
        morph.sendMorphLater(player);
    }

    /* From what I've read, player quit event won't get triggered if the 
     * player was kicked, so I guess we have to listen to both events */

    @EventHandler
    public void onPlayerLogsOff(PlayerQuitEvent event)
    {
        this.clearNearbyPlayers(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event)
    {
        this.clearNearbyPlayers(event.getPlayer().getUniqueId());
    }

    private void clearNearbyPlayers(UUID id)
    {
        MorphPlayer morph = morphs.get(id);

        if (morph != null)
        {
            morph.nearbyPlayers.clear();
        }
    }
}