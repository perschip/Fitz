package com.salvos.morphie.fitz.events.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        UUID getUUID = UUID.fromString(new Playerdatafilemethods(plugin).getDiscordString(discordID, "MinecraftUUID"));
        Player player = Bukkit.getPlayer(getUUID);

        if (new Playerdatafilemethods(this.plugin).checkDiscordFile(discordID).exists()) {
            String format = plugin.getConfig().getString("DiscordToMinecraftFormat").replace("MESSAGE", message);
            format = PlaceholderAPI.setPlaceholders(player, format);
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bD&7] &7" + userName + " &8» &f" + message));
        }
    }
}
