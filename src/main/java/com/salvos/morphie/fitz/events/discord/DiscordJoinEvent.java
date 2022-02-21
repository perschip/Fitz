package com.salvos.morphie.fitz.events.discord;
import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.DiscordMethods;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordJoinEvent extends ListenerAdapter {

    private Fitz plugin;

    public DiscordJoinEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override // USE THIS WHEN YOU WANT TO OVERRIDE A METHOD
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        TextChannel welcome = plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getJoinLeaveId());
        TextChannel info = plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getInformationID());
        TextChannel botCommands = plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getBotCommandsID());
        TextChannel rules = plugin.getBot().getTextChannelById(new DiscordMethods(plugin).getRulesID());

        EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(user, "Welcome!");
        eBuilder.setDescription("Welcome to Salvos, " + user.getAsMention() + "! If you are in need of any information go ahead and check out " + info.getAsMention() + "!");
        eBuilder.addField("-", "Beep Boop. Looking for my commands? To get a list of all my commands you can type `!help` in the " + botCommands.getAsMention() + " channel!", false);
        eBuilder.addField("-", "Lastly, you can find our servers rules within the" + rules.getAsMention() + "channel. Happy Crafting!", false);
        MessageEmbed embed = eBuilder.build();
        welcome.sendMessage((CharSequence) embed).queue();
    }
}
