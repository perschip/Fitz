package com.salvos.morphie.fitz.files;

import com.salvos.morphie.fitz.Fitz;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Playerdatafilemethods {
    private Fitz plugin;

    public Playerdatafilemethods(Fitz plugin) {
        this.plugin = plugin;
    }

    public String getString(UUID uuid, String string) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        return fc.getString(string);
    }

    public Boolean getBoolean(UUID uuid, String string) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        Boolean b = fc.getBoolean(string);
        return b;
    }

    public File getPlayerFile(UUID uuid) {
        File playerFile = new File(this.plugin.getDataFolder() + File.separator + "PlayerData", uuid + ".yml");
        FileConfiguration playerCFG = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerFile.exists()) {
            try {
                playerCFG.save(playerFile);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return playerFile;
    }

    public String getDiscordString(String discordID, String string) {
        File file = getDiscordFile(discordID);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        return fc.getString(string);
    }

    public File checkDiscordFile(String discordID) {
        File playerFile = new File(this.plugin.getDataFolder() + File.separator + "VerifiedUsers", discordID + ".yml");
        return playerFile;
    }

    public File getDiscordFile(String discordID) {
        File playerFile = new File(this.plugin.getDataFolder() + File.separator + "VerifiedUsers", discordID + ".yml");
        FileConfiguration playerCFG = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerFile.exists()) {
            try {
                playerCFG.save(playerFile);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return playerFile;
    }

    public void setBoolean(Player player, UUID uuid, String string, Boolean b) {
        File file = getPlayerFile(player.getUniqueId());
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        fc.set(string, Boolean.valueOf(b));
        try
        {
            fc.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setString(Player player, UUID uuid, String string, String string2) {
        File file = getPlayerFile(player.getUniqueId());
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        fc.set(string, String.valueOf(string2));
        try
        {
            fc.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
