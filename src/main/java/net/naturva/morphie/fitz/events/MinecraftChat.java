package net.naturva.morphie.fitz.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.md_5.bungee.api.ChatColor;
import net.naturva.morphie.fitz.Fitz;
import net.naturva.morphie.fitz.files.playerDataFileMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class MinecraftChat extends ListenerAdapter implements Listener {

    private Fitz plugin;

    public MinecraftChat(Fitz plugin) {
        this.plugin = plugin;
    }

    // Minecraft to discord channel event.
    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        Player p = e.getPlayer();
        String name = p.getName();
        TextChannel channel = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        Guild guild = this.plugin.jda.getGuildById("430081943120642048");
        if (new playerDataFileMethods(this.plugin).getBoolean(p.getUniqueId(), "Linked") == true) {
            if (new playerDataFileMethods(this.plugin).getBoolean(p.getUniqueId(), "DiscordRoleCreated") == false) {
                if (new playerDataFileMethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank") == "Member") {
                    channel.sendMessage("**" + p.getName() + "**" + " **»** " + msg).queue();
                } else {
                    String rank = new playerDataFileMethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank");
                    int intValue = Integer.parseInt(this.plugin.getConfig().getString(rank),16);
                    Color aColor = new Color( intValue );
                    RoleAction roleAction1 = guild.createRole();
                    roleAction1.setName(name);
                    roleAction1.setColor(aColor);
                    Role role = roleAction1.complete();
                    new playerDataFileMethods(this.plugin).setBoolean(p, p.getUniqueId(), "DiscordRoleCreated", true);
                    channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
                }
            } else if (new playerDataFileMethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank") == "Member") {
                channel.sendMessage("**" + p.getName() + "**" + " **»** " + msg).queue();
            } else {
                Role role = guild.getRolesByName(p.getName(), false).get(0);
                channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
            }
        } else {
            channel.sendMessage("*" + p.getName() + "*" + " **»** " + msg).queue();
        }
    }

    // Discord to minecraft chat event.
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        TextChannel channel = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        if (e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage())return;
        String message = e.getMessage().getContentRaw();
        if (e.getMessage().getChannel() != channel)return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase("!who"))return;
        User user = e.getAuthor();
        String userName = user.getName();
        String discordID = user.getId();

        if (new playerDataFileMethods(this.plugin).checkDiscordFile(discordID).exists()) {
            String format = new playerDataFileMethods(this.plugin).getDiscordString(discordID, "MinecraftFormat");
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bD&8] " + format + " &8» &f" + message));
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bD&7] &7[&8Knee&7-&8Gear&7] &f" + userName + " &8» &f" + message));
        }
    }

    // Message sent to the discord channel on player join. (Unless user has silent join perms.)
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName().toString();
        TextChannel channel = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        Guild guild = this.plugin.jda.getGuildById("430081943120642048");
        Role role = guild.getRolesByName("Fitz", false).get(0);
        if (p.hasPlayedBefore()) {
            if (!p.hasPermission("Fitz.silentJoin")) {
                channel.sendMessage(":arrow_forward: ** " + name + " has connected.**").queue();
            }
        } else {
            channel.sendMessage("**[**⚑**]** " + role.getAsMention() + " **»** **Welcome** " + name + " to the server!").queue();
        }
    }

    // Message sent to the discord channel on player leave. (Unless user has silent leave perms.)
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName().toString();
        TextChannel channel = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        if (!p.hasPermission("Fitz.silentLeave")) {
            this.plugin.chat.getGroupPrefix("world", "group");
            channel.sendMessage(":arrow_backward: ** " + name + " has disconnected.**").queue();
        }
    }
}
