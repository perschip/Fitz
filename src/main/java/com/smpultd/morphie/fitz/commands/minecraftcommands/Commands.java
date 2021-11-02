package com.smpultd.morphie.fitz.commands.minecraftcommands;

import com.smpultd.morphie.fitz.Fitz;
import net.dv8tion.jda.api.entities.*;
import net.md_5.bungee.api.ChatColor;
import com.smpultd.morphie.fitz.events.PlayerDataFileEvents;
import com.smpultd.morphie.fitz.files.Playerdatafilemethods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class Commands implements CommandExecutor {

    private Fitz plugin;

    public Commands(Fitz plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Fitz] Only players can run this command!");

        }
        Player player = (Player)sender;
        Guild guild = this.plugin.jda.getGuildById(this.plugin.getConfig().getString("Guild"));
        if (cmd.getName().equalsIgnoreCase("verify")) {
            if (new Playerdatafilemethods(this.plugin).getBoolean(player.getUniqueId(), "Linked") == true) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aYou have already verified your account! &8(&7" + new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "DiscordName") + "&8)"));
                return true;
            }
            if (!this.plugin.uuidUserCode.containsKey(player.getUniqueId())) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aPlease run the &7!link <MinecraftName> &acommand to get your verification code."));
                return true;
            }
            if (!(args.length == 1)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aCorrect Usage&8: &7/verify <code>"));
                return true;
            }
            String code = this.plugin.uuidUserCode.get(player.getUniqueId());
            if (!args[0].equals(code)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aInvalid verification code! Please try again."));
                return true;
            }
            String discordID = this.plugin.uuidDiscordId.get(player.getUniqueId());
            User user = this.plugin.jda.retrieveUserById(discordID).complete();
            Member member = guild.retrieveMember(user).complete();
            if (member == null) {
                this.plugin.uuidDiscordId.remove(player.getUniqueId());
                this.plugin.uuidUserCode.remove(player.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aCannot find your discord account! Are you on our server? (/discord)"));
                return true;
            }
            String prefix = this.plugin.chat.getPlayerPrefix(player);
            String format = ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName());
            new PlayerDataFileEvents(this.plugin).addData(player, discordID, member.getUser().getName(), format);
            new Playerdatafilemethods(this.plugin).setBoolean(player, player.getUniqueId(), "Linked", true);
            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "MinecraftName", player.getName());
            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "MinecraftRank", this.plugin.perms.getPrimaryGroup(player));
            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "DiscordName", member.getUser().getName() + "#" + member.getUser().getDiscriminator());
            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "DiscordId", this.plugin.uuidDiscordId.get(player.getUniqueId()));
            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "DiscordRole", member.getRoles().get(0).toString());
            this.plugin.uuidDiscordId.remove(player.getUniqueId());
            this.plugin.uuidUserCode.remove(player.getUniqueId());
            Role role = guild.getRolesByName(this.plugin.getConfig().getString("Verified"), false).get(0);
            Role role2 = guild.getRolesByName(this.plugin.getConfig().getString("Fitz"), false).get(0);
            guild.addRoleToMember(member, role).queue();
            TextChannel channel = this.plugin.jda.getTextChannelById(this.plugin.getConfig().getString("BridgeChannelID"));
            member.getUser().openPrivateChannel().complete().sendMessage(":white_check_mark: You successfully verified your account! **(**" + player.getName() + "**)**").queue();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aAccount successfully verified! &8(&7" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "&8)"));
            channel.sendMessage(role2.getAsMention() + " **»** " + member.getAsMention() + " just linked their discord and minecraft accounts!").queue();
            return true;
        } else if (cmd.getName().equalsIgnoreCase("discord")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8---------- [&3&lDiscord&8] ----------"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/discord resync &7- &aRe-sync your discord and minecraft accounts."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/discord unlink &7- &aUnlink your discord and minecraft accounts."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/verify <code> &7- &aUse the Code Fitz sends you on discord from the !link <mc name>."));
                sender.sendMessage("");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou can join our discord here! &7https://discord.gg/4MQndNS"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8---------- [&3&lDiscord&8] ----------"));
                return true;
            } else if (args[0].equals("resync")) {
                if (new Playerdatafilemethods(this.plugin).getBoolean(player.getUniqueId(), "Linked")) {
                    String discordID = new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "DiscordId");
                    Member member = guild.getMemberById(discordID);
                    if (member == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aCannot find your discord account! Are you on our server? (/discord)"));
                        return true;
                    } else if (this.plugin.perms.getPrimaryGroup(player).equals("default")) {
                        if (guild.getRolesByName(player.getName(), false).get(0) != null) {
                            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "MinecraftRank", this.plugin.perms.getPrimaryGroup(player));
                            String rank = new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "MinecraftRank");
                            int intValue = Integer.parseInt(this.plugin.getConfig().getString(rank),16);
                            Color aColor = new Color( intValue );
                            Role role = guild.getRolesByName(player.getName(), false).get(0);
                            role.getManager().setColor(aColor).complete();
                            new Playerdatafilemethods(this.plugin).setBoolean(player, player.getUniqueId(), "DiscordRoleCreated", true);
//                            new playerDataFileMethods(this.plugin).setString(player, player.getUniqueId(), "DiscordRole", member.getRoles().get(0).toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aResync succesfull! &8(&7" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "&8)"));
                            return true;
                        } else {
                            new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "MinecraftRank", this.plugin.perms.getPrimaryGroup(player));
//                            new playerDataFileMethods(this.plugin).setString(player, player.getUniqueId(), "DiscordRole", member.getRoles().get(0).toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aResync succesfull! &8(&7" + member.getUser().getName() + member.getUser().getDiscriminator() + "&8)"));
                            return true;
                        }
                    } else {
                        new Playerdatafilemethods(this.plugin).setString(player, player.getUniqueId(), "MinecraftRank", this.plugin.perms.getPrimaryGroup(player));
                        String rank = new Playerdatafilemethods(this.plugin).getString(player.getUniqueId(), "MinecraftRank");
                        int intValue = Integer.parseInt(this.plugin.getConfig().getString(rank),16);
                        Color aColor = new Color( intValue );
                        Role role = guild.getRolesByName(player.getName(), false).get(0);
                        role.getManager().setColor(aColor).complete();
                        new Playerdatafilemethods(this.plugin).setBoolean(player, player.getUniqueId(), "DiscordRoleCreated", true);
//                        new playerDataFileMethods(this.plugin).setString(player, player.getUniqueId(), "DiscordRole", member.getRoles().get(0).toString());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aResync succesfull! &8(&7" + member.getUser().getName() + "#" +  member.getUser().getDiscriminator() + "&8)"));
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aYou do not have a discord account linked! Join our &7/discord &aand run the &7!link <mc username> &ain the bot-commands channel!"));
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lDiscord&8] &aInvalid args! &7/discord resync"));
                return true;
            }
        }
        return false;
    }
}
