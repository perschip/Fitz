package net.naturva.morphie.fitz.commands.discordcommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.naturva.morphie.fitz.Fitz;
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
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage())return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        User user = e.getAuthor();
        TextChannel Bridge = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
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

                EmbedBuilder eBuilder = new EmbedBuilder();

                eBuilder.setAuthor("Naturva » Whos online?", null, "https://i.imgur.com/QhPRcUF.png");
                eBuilder.setColor(Color.CYAN);

                if (players.size() > 0) {
                    eBuilder.setDescription("\n \nCurrently **" + players.size() + "** players online.\n \n" + pl.toString());
                } else {
                    eBuilder.setDescription("\n \nCurrently **" + players.size() + "** players online.");
                }

                eBuilder.setFooter("All rights reserved, Naturva 2019 ↠", "https://i.imgur.com/QhPRcUF.png");

                MessageEmbed embed = eBuilder.build();

                e.getChannel().sendMessage(embed).queue();
            }
        }
    }
}
