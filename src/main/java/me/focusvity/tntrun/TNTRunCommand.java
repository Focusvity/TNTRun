package me.focusvity.tntrun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TNTRunCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("start"))
        {
            TNTRun.plugin.game.init();
            sender.sendMessage("Game has started");
            return true;
        }
        return false;
    }
}
