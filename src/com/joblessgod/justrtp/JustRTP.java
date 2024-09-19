package com.joblessgod.justrtp;

import com.joblessgod.justrtp.Commands.RTPCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class JustRTP extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("rtp").setExecutor(new RTPCommand(this));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[JustRTP] plugin has enabled!");
    }


    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[JustRTP] plugin has disabled!");
    }
}
