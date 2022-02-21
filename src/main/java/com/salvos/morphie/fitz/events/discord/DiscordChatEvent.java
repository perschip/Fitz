package com.salvos.morphie.fitz.events.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class DiscordChatEvent extends ListenerAdapter {

    private Fitz plugin;

    public DiscordChatEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        TextChannel channel = this.plugin.getBot().getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        if (e.getAuthor().isBot() || e.getAuthor().isBot() || e.isWebhookMessage())return;
        String message = e.getMessage().getContentRaw();
        if (e.getMessage().getChannel() != channel)return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase("!who"))return;
        User user = e.getAuthor();
        String userName = user.getName();
        String discordID = user.getId();

        if (new Playerdatafilemethods(this.plugin).checkDiscordFile(discordID).exists()) {
            String format = new Playerdatafilemethods(this.plugin).getDiscordString(discordID, "MinecraftFormat");
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bD&8] " + format + " &8» &f" + message));
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bD&7] &7" + userName + " &8» &f" + message));
        }
    }
}
