package com.salvos.morphie.fitz.util;

import com.salvos.morphie.fitz.Fitz;
import jdk.nashorn.internal.objects.annotations.Getter;

public class DiscordMethods {

    private Fitz plugin;

    public DiscordMethods(Fitz plugin) {
        this.plugin = plugin;
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
