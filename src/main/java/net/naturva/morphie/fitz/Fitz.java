package net.naturva.morphie.fitz;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.naturva.morphie.fitz.commands.discordcommands.DiscordLinkCommand;
import net.naturva.morphie.fitz.commands.discordcommands.WhoCommand;
import net.naturva.morphie.fitz.commands.minecraftcommands.Commands;
import net.naturva.morphie.fitz.events.MinecraftChat;
import net.naturva.morphie.fitz.events.playerDataFileEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Fitz extends JavaPlugin implements Listener {
	public JDA jda;
    public Permission perms = null;
    public Chat chat = null;
    public HashMap<UUID,String>uuidUserCode;
    public HashMap<UUID,String>uuidDiscordId;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MinecraftChat(this), this);
        getServer().getPluginManager().registerEvents(new playerDataFileEvents(this), this);
        getCommand("verify").setExecutor(new Commands(this));
        getCommand("discord").setExecutor(new Commands(this));
        startBot();
        uuidUserCode = new HashMap<>();
        uuidDiscordId = new HashMap<>();
        createConfig();
        setupPermissions();
        setupChat();
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bBot&8: &aOnline"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin Status&8: &aEnabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));

        jda.addEventListener(new MinecraftChat(this));
        jda.addEventListener(new WhoCommand(this));
        jda.addEventListener(new DiscordLinkCommand(this));
        TextChannel channel = jda.getTextChannelById(this.getConfig().getString("BridgeChannelID"));
        channel.sendMessage(this.getConfig().getString("BotLoaded")).queue();

        jda.getPresence().setActivity(Activity.watching(" on Smpultd.com"));
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bBot&8: &cOffline"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin Status&8: &cDisabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[----------[&3Fitz&8]----------]"));
        TextChannel channel = jda.getTextChannelById(this.getConfig().getString("BridgeChannelID"));
        channel.sendMessage(this.getConfig().getString("BotUnloaded")).queue();
    }

    private void startBot() {
        try {
            jda = JDABuilder.createDefault(this.getConfig().getString("Token")) // The token of the account that is logging in.
                    .build();
            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building JDA!");
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
}
