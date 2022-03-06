package com.salvos.morphie.fitz.commands.discord;

import com.salvos.morphie.fitz.Fitz;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingCommand extends ListenerAdapter {

    private Fitz plugin;

    public PingCommand(Fitz plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        if (!event.getName().equals("ping")) return;
        event.reply("Pong!").setEphemeral(false) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit

    }
}
