package com.salvos.morphie.fitz.events.minecraft;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.DiscordMethods;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinecraftJoinEvent implements Listener {

    private Fitz plugin;

    public MinecraftJoinEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    // Message sent to the discord channel on player join. (Unless user has silent join perms.)
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        TextChannel channel = this.plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getBridgeId());
        Guild guild = plugin.getBot().getGuildById(new DiscordMethods(plugin).getGuild());
        Role role = guild.getRolesByName("Fitz", false).get(0);
        if (p.hasPlayedBefore()) {
            if (!p.hasPermission("Fitz.silentJoin")) {
                channel.sendMessage(plugin.getConfig().getString("PlayerJoin").replace("%PLAYER%", name)).queue();
            }
        } else {
            channel.sendMessage(plugin.getConfig().getString("PlayerFirstJoin").replace("%FITZ%", role.getAsMention()).replace("%PLAYER%", name)).queue();
        }
        new DiscordMethods(plugin).setTopic("UPDATE");
    }
}
