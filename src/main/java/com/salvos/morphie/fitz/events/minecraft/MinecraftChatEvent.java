package com.salvos.morphie.fitz.events.minecraft;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import com.salvos.morphie.fitz.util.DiscordMethods;
import com.salvos.morphie.fitz.util.RankUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;

public class MinecraftChatEvent implements Listener {

    private Fitz plugin;

    public MinecraftChatEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    // Minecraft to discord channel event.
    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        Player p = e.getPlayer();
        String name = p.getName();
        String chatFormat = this.plugin.getConfig().getString("MinecraftToDiscordFormat").replace("%RANK%", plugin.chat.getPrimaryGroup(p)).replace("%PLAYER%", name);
        TextChannel channel = this.plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getBridgeId());
        Guild guild = this.plugin.getBot().getGuildById(new DiscordMethods(plugin).getGuild());
        String discordID = new Playerdatafilemethods(plugin).getString(p.getUniqueId(), "DiscordId");
        String rank = plugin.perms.getPrimaryGroup(p);

        // Replace @here or @everyone
        msg = msg.replace("@everyone", "everyone");
        msg = msg.replace("@here", "here");

        if (new Playerdatafilemethods(this.plugin).getBoolean(p.getUniqueId(), "Linked")) {
            if (!rank.equalsIgnoreCase("nerd")) {
                if (new Playerdatafilemethods(this.plugin).getBoolean(p.getUniqueId(), "DiscordRoleCreated")) {
                    Role role = guild.getRoleById(new Playerdatafilemethods(plugin).getString(p.getUniqueId(),"RoleId"));
                    Color roleColor = role.getColor();
                    String getSetColor = new Playerdatafilemethods(plugin).getDiscordString(discordID, "Color");
                    if (getSetColor != null && roleColor.toString().equalsIgnoreCase(getSetColor)) {
                        channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
                    } else {
                        new RankUtils(plugin).updateRank(p);
                        String newRank = new RankUtils(plugin).getRank(p);
                        int intValue = Integer.parseInt(this.plugin.getConfig().getString(newRank), 16);
                        Color aColor = new Color(intValue);
                        role.getManager().setColor(aColor).complete();
                        new Playerdatafilemethods(plugin).setDiscordString(discordID, "DiscordColor", aColor.toString());
                        channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
                    }
                } else {
                    int intValue = Integer.parseInt(this.plugin.getConfig().getString(rank), 16);
                    Color aColor = new Color(intValue);
                    RoleAction roleAction1 = guild.createRole();
                    roleAction1.setName(name);
                    roleAction1.setColor(aColor);
                    Role role = roleAction1.complete();
                    new Playerdatafilemethods(plugin).setString(p,p.getUniqueId(),"RoleId", role.getId());
                    new Playerdatafilemethods(plugin).setDiscordString(discordID, "DiscordColor", aColor.toString());
                    new Playerdatafilemethods(this.plugin).setBoolean(p, p.getUniqueId(), "DiscordRoleCreated", true);
                    channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
                }
            } else {
                new RankUtils(plugin).removeRole(p);
                channel.sendMessage(chatFormat + msg).queue();
            }
        } else {
            channel.sendMessage(chatFormat + msg).queue();
        }
    }
}
