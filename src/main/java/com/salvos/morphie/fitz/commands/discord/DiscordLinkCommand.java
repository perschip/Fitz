package com.salvos.morphie.fitz.commands.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class DiscordLinkCommand extends ListenerAdapter {

    private Fitz plugin;

    public DiscordLinkCommand(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("link")) return;
            if (event.getOption("mcname") != null) {
                Player player = Bukkit.getPlayer(event.getOption("mcname").toString());
                if (player == null) {
                    event.reply(":no_entry: I cannot find that user on the server currently! *(Make sure you're connected to the minecraft server AND spelling your mc username properly!)*").setEphemeral(true).queue();
                    return;
                }
                if (new Playerdatafilemethods(this.plugin).getBoolean(player.getUniqueId(), "Linked")) {
                    event.reply(":no_entry: You have already verified your account! **(**" + new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "DiscordName") + "**)**").setEphemeral(true).queue();
                    return;
                }
                String randomCode = new Random().nextInt(8000)+2000+"GG";
                User user = event.getUser();
                this.plugin.uuidUserCode.put(player.getUniqueId(), randomCode);
                this.plugin.uuidDiscordId.put(player.getUniqueId(), event.getUser().getId());

                EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(user, "Discord Verification!");
                eBuilder.setDescription("Hello, " + user.getAsMention() + "!\n \nTo complete your verification please type the following command in-game!\n`/verify " + randomCode.toString() + "`\n \nIf you run into any problems feel free to contact an avalible staff member!");
                MessageEmbed embed = eBuilder.build();
                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(embed).queue();
                event.reply(":white_check_mark: I have private messaged you further linking instructions.").setEphemeral(true).queue();

            } else {
                event.reply(":no_entry: Invalid: `/link MCNAME` - Make sure your minecraft account is currently connected to the server and you spell its username properly!").setEphemeral(true).queue();
            }

    }
}
