package com.salvos.morphie.fitz.commands.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class WhoCommand extends ListenerAdapter {

    private Fitz plugin;

    public WhoCommand(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot() || e.getAuthor().isBot() || e.isWebhookMessage())return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        User user = e.getAuthor();
        TextChannel Bridge = this.plugin.getBot().getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
        if (args.length > 1) {
            return;
        } if (e.getChannel() != Bridge) {
            return;
        } else {
            if (args[0].equalsIgnoreCase("!who")) {

                Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
                ArrayList<String> pl = new ArrayList();;
                for(Player p : players) {
                    pl.add(p.getName());
                }

                EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(user, "Whos Online?");
                if (players.size() > 0) {
                    eBuilder.setDescription("\n \nCurrently **" + players.size() + "** players online.\n \n" + pl.toString());
                } else {
                    eBuilder.setDescription("\n \nCurrently **" + players.size() + "** players online.");
                }
                MessageEmbed embed = eBuilder.build();
                e.getChannel().sendMessageEmbeds(embed).queue();
            }
        }
    }
}