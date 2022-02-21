package com.salvos.morphie.fitz.commands.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
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
    public void onMessageReceived(MessageReceivedEvent e) {
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
            if (new Playerdatafilemethods(this.plugin).getBoolean(player.getUniqueId(), "Linked")) {
                e.getChannel().sendMessage(":no_entry: You have already verified your account! **(**" + new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "DiscordName") + "**)**").queue();
                return;
            }
            String randomCode = new Random().nextInt(8000)+2000+"NA";
            User user = e.getAuthor();
            this.plugin.uuidUserCode.put(player.getUniqueId(), randomCode);
            this.plugin.uuidDiscordId.put(player.getUniqueId(), e.getAuthor().getId());

            EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(user, "Discord Verification!");
            eBuilder.setDescription("Hello, " + user.getAsMention() + "!\n \nTo complete your verification please type the following command in-game!\n`/verify " + randomCode.toString() + "`\n \nIf you run into any problems feel free to contact an avalible staff member!");
            MessageEmbed embed = eBuilder.build();
            e.getAuthor().openPrivateChannel().complete().sendMessageEmbeds(embed).queue();
            e.getChannel().sendMessage(":white_check_mark: Instructions sent in PMs! :)").queue();
        }
    }
}
