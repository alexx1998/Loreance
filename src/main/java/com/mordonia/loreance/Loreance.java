package com.mordonia.loreance;

import com.mordonia.loreance.chat.DiscordIntegration;
import com.mordonia.mcore.MCore;
import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class Loreance extends JavaPlugin {
    private JDA jda;
    public MCore mcoreAPI = (MCore) Bukkit.getServer().getPluginManager().getPlugin("MCore");


    @Override
    public void onEnable() {
        startBot();
        loadConfig();
        PlayerChatDataManager playerChatDataManager = mcoreAPI.playerChatDataManager;
        this.getServer().getPluginManager().registerEvents(new DiscordIntegration(this, jda, playerChatDataManager), this);
        jda.addEventListener(new DiscordIntegration(this, jda, playerChatDataManager));

    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }

    private void startBot() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(this.getConfig().getString("discord.token")).build();

        } catch (LoginException | NoClassDefFoundError e) {
            this.getServer().getConsoleSender().sendMessage("Class not found!");
        }
    }

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }
}
