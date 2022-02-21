package com.salvos.morphie.fitz.events.discord;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordLeaveEvent extends ListenerAdapter {

    private Fitz plugin;

    public DiscordLeaveEvent(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        User user = e.getUser();
        TextChannel leave = this.plugin.getBot().getTextChannelById("786248214012887098");

        EmbedBuilder eBuilder = new EmbedUtils(plugin).embedBuilder(user, "Bye!");
        eBuilder.addField("-", "Farewell, " + user.getName() + "!", false);
        MessageEmbed embed = eBuilder.build();

        leave.sendMessageEmbeds(embed).queue();
    }
}
