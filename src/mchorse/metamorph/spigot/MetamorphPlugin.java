package mchorse.metamorph.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import mchorse.metamorph.spigot.commands.CommandMorph;

/**
 * Metamorph plugin 
 * 
 * This bad boy is responsible for providing a /morph command and 
 * syncing morphs between players.
 * 
 * Too bad that Spigot doesn't have a proper tracking system. I guess 
 * it might've been too ExPEnSivE... REEEEE
 */
public class MetamorphPlugin extends JavaPlugin
{
    public static MetamorphPlugin instance;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getCommand("morph").setExecutor(new CommandMorph(this));

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Metamorph");
        Bukkit.getPluginManager().registerEvents(new MetamorphListener(), this);
        MetamorphTask.task.runTaskTimer(this, 40L, 5L);
    }

    @Override
    public void onDisable()
    {
        /* This is probably is an overkill */
        MetamorphListener.morphs.clear();
    }
}