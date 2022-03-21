package com.salvos.morphie.fitz.commands.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {

    private Fitz plugin;

    public HelpCommand(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("help")) return;
            EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(event.getUser(), "Fitz Commands:");
            eBuilder.setDescription("Below you will find a list of commands outlined in categories for ease of understanding. Some of these commands will lead to additional help embeds that will only be visible to the player that runs the command. Additionally if you require further help you can contact any staff member currently online.\n \n**Discord Related Commands »**\n \n/ping - PONG! This is a simple command to return the bots ping to our mc server.\n \n/link <MCNAME> - This command is to link your Minecraft and Discord accounts. Run `/link` without any args to return more information.\n \n**THIS IS A WIP**");
            MessageEmbed embed = eBuilder.build();
            event.replyEmbeds(embed).queue();
    }
}
