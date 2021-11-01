package com.smpultd.morphie.fitz.commands.discordcommands;

import com.smpultd.morphie.fitz.Fitz;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.smpultd.morphie.fitz.files.Playerdatafilemethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Random;

public class DiscordLinkCommand extends ListenerAdapter {

    private Fitz plugin;

    public DiscordLinkCommand(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot() || e.getAuthor().isBot() || e.isWebhookMessage())return;
        String[] args = e.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase("!link")) {
            if (args.length != 2) {
                e.getChannel().sendMessage(":no_entry: Correct Usage: *!link <Minecraft Username>*").queue();
                return;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                e.getChannel().sendMessage(":no_entry: I cannot find that user on the server currently! *(Make sure you're connected to the server!)*").queue();
                return;
            }
            if (new Playerdatafilemethods(this.plugin).getBoolean(player.getUniqueId(), "Linked") == true) {
                e.getChannel().sendMessage(":no_entry: You have already verified your account! **(**" + new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "DiscordName") + "**)**").queue();
                return;
            }
            String randomCode = new Random().nextInt(8000)+2000+"NA";
            User user = e.getAuthor();
            this.plugin.uuidUserCode.put(player.getUniqueId(), randomCode);
            this.plugin.uuidDiscordId.put(player.getUniqueId(), e.getAuthor().getId());

            EmbedBuilder eBuilder = new EmbedBuilder();

            eBuilder.setAuthor("SMP-Ultd » Discord Verification!", null, "https://i.imgur.com/Uc7xoQ9.png");
            eBuilder.setColor(Color.decode("#314ecc"));
            eBuilder.setThumbnail(user.getEffectiveAvatarUrl());
            eBuilder.setDescription("Hello, " + user.getAsMention() + "!\n \nTo complete your verification please type the following command in-game!\n`/verify " + randomCode.toString() + "`\n \nIf you run into any problems feel free to contact an avalible staff member!");
            eBuilder.setFooter("All rights reserved, SMP-Ultd 2021 ↠", "https://i.imgur.com/Uc7xoQ9.png");

            MessageEmbed embed = eBuilder.build();

            e.getAuthor().openPrivateChannel().complete().sendMessage(embed).queue();
            e.getChannel().sendMessage(":white_check_mark: Instructions sent in PMs! :)").queue();
        }
    }
}