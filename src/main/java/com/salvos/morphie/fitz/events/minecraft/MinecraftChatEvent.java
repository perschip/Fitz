package com.salvos.morphie.fitz.events.minecraft;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import com.salvos.morphie.fitz.util.DiscordMethods;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
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
        String chatFormat = this.plugin.getConfig().getString("MinecraftToDiscordFormat").replace("%RANK%", plugin.chat.getPrimaryGroup(p)).replace( "%PLAYER%", name);
        TextChannel channel = this.plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getBridgeId());
        Guild guild = this.plugin.getBot().getGuildById(new DiscordMethods(plugin).getGuild());

        // Replace @here or @everyone
        msg = msg.replace("@everyone", "everyone");
        msg = msg.replace("@here", "here");

        // Check for any @'s in the message
        msg = new DiscordMethods(plugin).parseMentions(msg);

        if (new Playerdatafilemethods(this.plugin).getBoolean(p.getUniqueId(), "Linked")) {
            if (!new Playerdatafilemethods(this.plugin).getBoolean(p.getUniqueId(), "DiscordRoleCreated")) {
                if (new Playerdatafilemethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank") == "Member") {
                    channel.sendMessage(chatFormat + msg).queue();
                } else {
                    String rank = new Playerdatafilemethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank");
                    int intValue = Integer.parseInt(this.plugin.getConfig().getString(rank),16);
                    Color aColor = new Color( intValue );
                    RoleAction roleAction1 = guild.createRole();
                    roleAction1.setName(name);
                    roleAction1.setColor(aColor);
                    Role role = roleAction1.complete();
                    new Playerdatafilemethods(this.plugin).setBoolean(p, p.getUniqueId(), "DiscordRoleCreated", true);
                    channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
                }
            } else if (new Playerdatafilemethods(this.plugin).getString(p.getUniqueId(), "MinecraftRank") == "Member") {
                channel.sendMessage(chatFormat + msg).queue();
            } else {
                Role role = guild.getRolesByName(p.getName(), false).get(0);
                channel.sendMessage(role.getAsMention() + " **»** " + msg).queue();
            }
        } else {
            channel.sendMessage(chatFormat + msg).queue();
        }
    }
}
