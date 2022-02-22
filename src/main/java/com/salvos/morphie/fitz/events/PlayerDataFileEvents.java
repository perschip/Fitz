package com.salvos.morphie.fitz.events;

import com.salvos.morphie.fitz.Fitz;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataFileEvents implements Listener {

    private Fitz plugin;

    public PlayerDataFileEvents(Fitz plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            public void run() {
                File file = getData(uuid);
                FileConfiguration pd = YamlConfiguration.loadConfiguration(file);
                if (!pd.contains("Linked")) {
                    pd.set("Linked", Boolean.valueOf(false));
                    pd.set("DiscordRoleCreated", Boolean.valueOf(false));
                    pd.set("MinecraftName", String.valueOf("null"));
                    pd.set("MinecraftRank", String.valueOf("null"));
                    pd.set("DiscordName", String.valueOf("null"));
                    pd.set("DiscordId", String.valueOf("null"));
                    pd.set("DiscordRole", String.valueOf("null"));
                    try {
                        pd.save(file);
                    }
                    catch (IOException e) {
                        Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + uuid + "'s player file!");
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskLater(this.plugin, 60L);
    }

    public File getData(UUID uuid) {
        File data = new File(this.plugin.getDataFolder() + File.separator + "PlayerData", uuid + ".yml");
        FileConfiguration dataFile = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            try {
                dataFile.save(data);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return data;
    }

    public void addData(final Player player, final String discordID, final String discordName) {
        final UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            public void run() {
                File file = getDiscordData(discordID);
                FileConfiguration pd2 = YamlConfiguration.loadConfiguration(file);
                if (!pd2.contains("MinecraftName")) {
                    pd2.set("MinecraftName", String.valueOf(player.getName()));
                    pd2.set("MinecraftUUID", String.valueOf(uuid));
                    pd2.set("DiscordName", String.valueOf(discordName));
                    try {
                        pd2.save(file);
                    }
                    catch (IOException e) {
                        Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + discordID + "'s player file!");
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskLater(this.plugin, 60L);
    }

    public File getDiscordData(String discordID) {
        File data = new File(this.plugin.getDataFolder() + File.separator + "VerifiedUsers", discordID + ".yml");
        FileConfiguration dataFile = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            try {
                dataFile.save(data);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return data;
    }
}
