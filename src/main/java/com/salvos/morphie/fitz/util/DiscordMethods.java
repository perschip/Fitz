package com.salvos.morphie.fitz.util;

import com.salvos.morphie.fitz.Fitz;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class DiscordMethods {

    private Fitz plugin;

    public DiscordMethods(Fitz plugin) {
        this.plugin = plugin;
    }

    // Set the channel topic of the bridge.
    public void setTopic(String type) {
        Guild guild = plugin.getBot().getGuildById(this.getGuild());
        TextChannel bridge = guild.getTextChannelById(this.getBridgeId());
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        ArrayList<String> pl = new ArrayList();;
        for(Player p : players) {
            pl.add(p.getName());
        }

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int offlinePlayers = Bukkit.getOfflinePlayers().length;

        if (type.equalsIgnoreCase("UPDATE")) {
            String format = plugin.getConfig().getString("BridgeTopicFormat").replace("TYPE", "Server Online").replace("JOINS", ""+(onlinePlayers+offlinePlayers)).replace("PLAYERS", pl.toString());
            bridge.getManager().setTopic(format).queue();
        } else {
            String format = plugin.getConfig().getString("BridgeTopicFormat").replace("TYPE", "Server Offline").replace("JOINS", ""+(onlinePlayers+offlinePlayers)).replace("PLAYERS", pl.toString());
            bridge.getManager().setTopic(format).queue();
        }
    }

    // Get the ID of the Guild from the config
    @Getter
    public String getGuild() {
        return plugin.getConfig().getString("Guild");
    }
    // Get the ID of the discord channel used for the discord -> minecraft chat from the config
    @Getter
    public String getBridgeId() {
        return plugin.getConfig().getString("BridgeChannelID");
    }
    // Get the ID of the discord channel used for the discord join and leave messages
    @Getter
    public String getJoinLeaveId() {
        return plugin.getConfig().getString("JoinLeaveChannelID");
    }
    // Get the ID of the discord channel used for server information
    @Getter
    public String getInformationID() {
        return plugin.getConfig().getString("InformationChannelID");
    }
    // Get the ID of the discord channel used for server rules
    @Getter
    public String getRulesID() {
        return plugin.getConfig().getString("RulesChannelID");
    }
    // Get the ID of the discord channel used for server bot commands
    @Getter
    public String getBotCommandsID() {
        return plugin.getConfig().getString("BotCommandsChannelID");
    }
    // Get the image used within the embeded messages.
    @Getter
    public String getEmbedImage() {
        return plugin.getConfig().getString("Image");
    }
    // Get the default header for the embeded messages.
    @Getter
    public String getEmbedHeader() {
        return plugin.getConfig().getString("Header");
    }
    // Get the default footer for the embeded messages.
    @Getter
    public String getEmbedFooter() {
        return plugin.getConfig().getString("Footer");
    }
    // Get the color for the embeded messages.
    @Getter
    public String getEmbedColor() {
        return plugin.getConfig().getString("Color");
    }
}
