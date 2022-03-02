package com.salvos.morphie.fitz.util;

import com.salvos.morphie.fitz.Fitz;
import com.salvos.morphie.fitz.files.Playerdatafilemethods;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.entity.Player;

public class RankUtils {

    private Fitz plugin;

    public RankUtils(Fitz plugin) {
        this.plugin = plugin;
    }

    public void updateRank(Player p) {
        String currentRank = plugin.perms.getPrimaryGroup(p);
        String setRank = new Playerdatafilemethods(plugin).getString(p.getUniqueId(), "MinecraftRank");

        if (!currentRank.equalsIgnoreCase(setRank)) {
            new Playerdatafilemethods(plugin).setString(p, p.getUniqueId(), "MinecraftRank", plugin.perms.getPrimaryGroup(p));
        }
    }

    public void removeRole(Player p) {
        new Playerdatafilemethods(plugin).setBoolean(p,p.getUniqueId(),"DiscordRoleCreated", false);

        Guild guild = this.plugin.getBot().getGuildById(new DiscordMethods(plugin).getGuild());
        Role role = guild.getRoleById(new Playerdatafilemethods(plugin).getString(p.getUniqueId(),"RoleId"));
        role.delete().complete();
    }

    @Getter
    public String getRank(Player p) {
        return new Playerdatafilemethods(plugin).getString(p.getUniqueId(), "MinecraftRank");
    }
}
