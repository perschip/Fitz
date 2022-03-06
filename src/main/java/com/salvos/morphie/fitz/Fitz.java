package com.salvos.morphie.fitz;

import com.salvos.morphie.fitz.commands.discord.DiscordLinkCommand;
import com.salvos.morphie.fitz.commands.discord.PingCommand;
import com.salvos.morphie.fitz.commands.discord.WhoCommand;
import com.salvos.morphie.fitz.events.discord.DiscordChatEvent;
import com.salvos.morphie.fitz.events.discord.DiscordJoinEvent;
import com.salvos.morphie.fitz.events.discord.DiscordLeaveEvent;
import com.salvos.morphie.fitz.events.minecraft.MinecraftChatEvent;
import com.salvos.morphie.fitz.events.minecraft.MinecraftJoinEvent;
import com.salvos.morphie.fitz.events.minecraft.MinecraftLeaveEvent;
import com.salvos.morphie.fitz.util.DiscordMethods;
import jdk.nashorn.internal.objects.annotations.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import com.salvos.morphie.fitz.commands.minecraft.Commands;
import com.salvos.morphie.fitz.events.PlayerDataFileEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Fitz extends JavaPlugin implements Listener {

    private JDA bot;
    private Guild guild;
    private PlaceholderAPI p;

    public Permission perms = null;
    public Chat chat = null;
    public HashMap<UUID,String>uuidUserCode;
    public HashMap<UUID,String>uuidDiscordId;
    private MinecraftChatEvent mc;
    private MinecraftJoinEvent mj;
    private MinecraftLeaveEvent ml;
    private PlayerDataFileEvents pe;

    @Override
    public void onEnable() {
        getCommand("verify").setExecutor(new Commands(this));
        getCommand("discord").setExecutor(new Commands(this));
        this.mc = new MinecraftChatEvent(this);
        this.mj = new MinecraftJoinEvent(this);
        this.ml = new MinecraftLeaveEvent(this);
        this.pe = new PlayerDataFileEvents(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(this.mc, this);
        getServer().getPluginManager().registerEvents(this.pe, this);
        getServer().getPluginManager().registerEvents(this.mj, this);
        getServer().getPluginManager().registerEvents(this.ml, this);
        uuidUserCode = new HashMap<>();
        uuidDiscordId = new HashMap<>();
        createConfig();
        setupPermissions();
        setupChat();

        //Start Fitz
        startBot();

        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bBot&8: &aOnline"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin Status&8: &aEnabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bBot&8: &cOffline"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin Status&8: &cDisabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        TextChannel channel = bot.getTextChannelById(this.getConfig().getString("BridgeChannelID"));
        channel.sendMessage(this.getConfig().getString("BotUnloaded")).queue();
        new DiscordMethods(this).setTopic("OFFLINE");
    }

    private void startBot() {
        try {
            this.bot = JDABuilder.createDefault(getConfig().getString("Token"))
                    .build();
            bot.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");
            guild = bot.getGuildById(new DiscordMethods(this).getGuild());
            bot.addEventListener(new DiscordChatEvent(this));
            bot.addEventListener(new DiscordJoinEvent(this));
            bot.addEventListener(new DiscordLeaveEvent(this));
            bot.addEventListener(new WhoCommand(this));
            bot.addEventListener(new DiscordLinkCommand(this));
            bot.addEventListener(new PingCommand(this));

            //Commands: Propagating as the guild.
            guild.upsertCommand("ping", "Calculate ping of the bot.").queue();
            guild.upsertCommand("who", "Check who is connected to the MC server.").queue();
            guild.upsertCommand("link", "Link your MC and Discord accounts!")
                    .addOption(OptionType.STRING, "mcname", "MC name to link your discord account too")
                    .queue();

            bot.getPresence().setActivity(Activity.playing(getConfig().getString("StatusMessage")));

            new DiscordMethods(this).setTopic("UPDATE");
            TextChannel channel = bot.getTextChannelById(getConfig().getString("BridgeChannelID"));
            channel.sendMessage(getConfig().getString("BotLoaded")).queue();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bConfig&8: &aGenerating config."));
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            }
            else {
                getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bConfig&8: &aLoading config."));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission)rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    @Getter
    public JDA getBot() {
        return bot;
    }

    @Getter
    public PlaceholderAPI getPAPI() {
        return p;
    }
}
