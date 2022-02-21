package com.salvos.morphie.fitz.util;

import com.salvos.morphie.fitz.Fitz;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedUtils {

    private Fitz plugin;

    public EmbedUtils(Fitz plugin) {
        this.plugin = plugin;
    }

    public EmbedBuilder embedBuilder(User user, String title) {
        EmbedBuilder eBuilder = new EmbedBuilder();

        eBuilder.setAuthor(plugin.getConfig().getString("Header").replace("TITLE", title));
        eBuilder.setColor(Color.decode(plugin.getConfig().getString("Color")));
        eBuilder.setFooter(plugin.getConfig().getString("Footer"), plugin.getConfig().getString("Image"));
        return eBuilder;
    }
}
