package com.salvos.morphie.fitz.events.minecraft;

import com.salvos.morphie.fitz.Fitz;
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
        String name = p.getName().toString();
        TextChannel channel = this.plugin.getBot().getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        Guild guild = this.plugin.getBot().getGuildById("430081943120642048");
        Role role = guild.getRolesByName("Fitz", false).get(0);
        if (p.hasPlayedBefore()) {
            if (!p.hasPermission("Fitz.silentJoin")) {
                channel.sendMessage(":arrow_forward: ** " + name + " has connected.**").queue();
            }
        } else {
            channel.sendMessage("**[**⚑**]** " + role.getAsMention() + " **»** **Welcome** " + name + " to the server!").queue();
        }
    }
}
