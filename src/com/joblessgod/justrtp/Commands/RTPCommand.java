package com.joblessgod.justrtp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class RTPCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public RTPCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        boolean useWorldBorder = plugin.getConfig().getBoolean("UseWorldBorder");
        int maxRadius = plugin.getConfig().getInt("MaxRadius");
        int minRadius = plugin.getConfig().getInt("MinRadius");
        int centerX = plugin.getConfig().getInt("CenterX");
        int centerZ = plugin.getConfig().getInt("CenterZ");
        int maxY = plugin.getConfig().getInt("MaxY");
        int minY = plugin.getConfig().getInt("MinY");
        int countdown = plugin.getConfig().getInt("RTPCountdown", 5); // Default 5s countdown
        String shape = plugin.getConfig().getString("Shape");

        List<String> allowedBiomes = plugin.getConfig().getStringList("Biomes");

        player.sendMessage(ChatColor.RED + "Don't MOVE!!!");

        new BukkitRunnable() {
            int timer = countdown;

            @Override
            public void run() {
                if (timer == 0) {
                    Random random = new Random();
                    int x = getRandomCoordinate(minRadius, maxRadius, shape, random, centerX);
                    int z = getRandomCoordinate(minRadius, maxRadius, shape, random, centerZ);
                    int y = getRandomSolidY(minY, maxY, world, x, z);

                    Biome biome = world.getBiome(x, z);
                    if (!allowedBiomes.isEmpty() && !allowedBiomes.contains(biome.name())) {
                        player.sendMessage("Teleport location is not in an allowed biome.");
                        cancel();
                        return;
                    }

                    Location randomLocation = new Location(world, x, y, z);
                    player.teleport(randomLocation, PlayerTeleportEvent.TeleportCause.COMMAND);

                    // Send title feedback to the player
                    String coordsMessage = ChatColor.GOLD + "X: " + x + " Y: " + y + " Z: " + z;
                    player.sendTitle(ChatColor.GOLD + "Teleported", coordsMessage, 10, 70, 20);

                    cancel();
                } else {
                    player.sendMessage(ChatColor.RED + "Teleporting in " + timer + " seconds...");
                    timer--;
                }
            }
        }.runTaskTimer(plugin, 0, 20); // 20 ticks = 1 second

        return true;
    }

    private int getRandomCoordinate(int minRadius, int maxRadius, String shape, Random random, int center) {
        if ("square".equalsIgnoreCase(shape)) {
            return center + (random.nextInt((maxRadius - minRadius) + 1) + minRadius) * (random.nextBoolean() ? 1 : -1);
        }
        // Handle other shapes, e.g., circle
        return center; // Placeholder for other shapes
    }

    private int getRandomSolidY(int minY, int maxY, World world, int x, int z) {
        for (int y = maxY; y >= minY; y--) {
            Material blockMaterial = world.getBlockAt(x, y, z).getType();
            if (blockMaterial.isSolid()) {
                return y + 1; // Return the first solid block's y-level above
            }
        }
        return minY; // Fallback if no suitable Y is found
    }
}
