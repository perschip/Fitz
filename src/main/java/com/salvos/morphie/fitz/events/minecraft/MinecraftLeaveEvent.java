package com.salvos.morphie.fitz.events.minecraft;

import com.salvos.morphie.fitz.Fitz;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MinecraftLeaveEvent implements Listener {

    private Fitz plugin;

    public MinecraftLeaveEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    // Message sent to the discord channel on player leave. (Unless user has silent leave perms.)
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName().toString();
        TextChannel channel = this.plugin.getBot().getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        if (!p.hasPermission("Fitz.silentLeave")) {
            this.plugin.chat.getGroupPrefix("world", "group");
            channel.sendMessage(":arrow_backward: ** " + name + " has disconnected.**").queue();
        }
    }
}
