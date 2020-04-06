package me.focusvity.tntrun;

import org.bukkit.plugin.java.JavaPlugin;

public final class TNTRun extends JavaPlugin
{

    public static TNTRun plugin;
    public Game game;

    @Override
    public void onEnable()
    {
        this.plugin = this;
        getServer().getPluginManager().registerEvents(new TNTRunListener(), this);
        saveDefaultConfig();
        this.game = new Game();
        getCommand("start").setExecutor(new TNTRunCommand());
    }

    @Override
    public void onDisable()
    {
        this.plugin = null;
    }
}
