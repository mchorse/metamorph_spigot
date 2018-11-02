package mchorse.metamorph.spigot.commands;

import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mchorse.metamorph.spigot.MetamorphListener;
import mchorse.metamorph.spigot.MetamorphPlugin;
import mchorse.metamorph.spigot.MorphPlayer;

/**
 * Morph command - /morph <player> <morph> [data_tag]
 * 
 * Morphs the given player into given morph. Works the same way as 
 * Metamorph's /morph command.
 */
public class CommandMorph implements CommandExecutor
{
    public MetamorphPlugin plugin;

    public CommandMorph(MetamorphPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        /* Morph */
        if (args.length >= 2)
        {
            String morph = args[1];

            for (int i = 2; i < args.length; i++)
            {
                morph += " " + args[i];
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player != null)
            {
                String full = args[0] + " " + morph;
                byte[] bytes = full.getBytes(Charset.forName("UTF-8"));
                MorphPlayer morphPlayer = MetamorphListener.morphs.get(player.getUniqueId());

                morphPlayer.morph = full;

                for (Player onlinePlayer : morphPlayer.nearbyPlayers.values())
                {
                    onlinePlayer.sendPluginMessage(this.plugin, "Metamorph", bytes);
                }

                player.sendPluginMessage(this.plugin, "Metamorph", bytes);

                return true;
            }
        }
        /* Demorph */
        else if (args.length == 1)
        {
            Player player = Bukkit.getPlayer(args[0]);

            if (player != null)
            {
                byte[] bytes = args[0].getBytes(Charset.forName("UTF-8"));
                MorphPlayer morphPlayer = MetamorphListener.morphs.get(player.getUniqueId());

                morphPlayer.morph = args[0];

                for (Player onlinePlayer : morphPlayer.nearbyPlayers.values())
                {
                    onlinePlayer.sendPluginMessage(this.plugin, "Metamorph", bytes);
                }

                player.sendPluginMessage(this.plugin, "Metamorph", bytes);

                return true;
            }
        }

        return false;
    }
}