package com.mordonia.loreance.chat;

import com.mordonia.loreance.Loreance;


import com.mordonia.mcore.mchat.util.playerData.PlayerChatDataManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscordIntegration extends ListenerAdapter implements Listener {
    private JDA jda;
    private Loreance plugin;
    private PlayerChatDataManager playerChatDataManager;

    public DiscordIntegration(Loreance plugin, JDA jda, PlayerChatDataManager playerChatDataManager) {
        this.jda = jda;
        this.plugin = plugin;
        this.playerChatDataManager = playerChatDataManager;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        if(event.getMessage().startsWith("@")){
            TextChannel channel = jda.getTextChannelsByName(plugin.getConfig().getString("discord.channel"), true).get(0);
            String firstname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getFirstname());
            String lastname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getLastname());
            String format = firstname + " " +  lastname + ": " +  event.getMessage().substring(1);
            channel.sendMessage(format).queue();
            if(event.getMessage().contains("joke")){
                List<String> jokes = plugin.getConfig().getStringList("jokes");
                int num = new Random().nextInt(jokes.size());
                channel.sendMessage("-" + jokes.get(num)).queue();
            }
            if(event.getMessage().contains("love")){
                List<String> love = plugin.getConfig().getStringList("love");
                int num = new Random().nextInt(love.size());
                channel.sendMessage(love.get(num)).queue();
            }
            if(event.getMessage().contains("married") || event.getMessage().contains("marry")){
                List<String> marry = plugin.getConfig().getStringList("marry");
                int num = new Random().nextInt(marry.size());
                channel.sendMessage(marry.get(num)).queue();
            }
            if(event.getMessage().contains("Hello Loreance") || event.getMessage().contains("Hey Loreance")){
                List<String> hello = plugin.getConfig().getStringList("hello");
                int num = new Random().nextInt(hello.size());
                channel.sendMessage(hello.get(num)).queue();
            }

        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event){
        TextChannel channel = jda.getTextChannelsByName(plugin.getConfig().getString("discord.channel"), true).get(0);
        String firstname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getFirstname());
        String lastname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getLastname());
        String format = ChatColor.stripColor("***" + firstname +  " [" + event.getPlayer().getDisplayName() + "] " + lastname + " has joined!***");
        channel.sendMessage(format).queue();
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent event){
        TextChannel channel = jda.getTextChannelsByName(plugin.getConfig().getString("discord.channel"), true).get(0);
        String firstname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getFirstname());
        String lastname = ChatColor.stripColor(playerChatDataManager.get(event.getPlayer()).getLastname());
        String format = ChatColor.stripColor("***" + firstname +  " [" + event.getPlayer().getDisplayName() + "] " + lastname + " has left!***");
        channel.sendMessage(format).queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        JDA jda = plugin.getJda();
        User user = event.getAuthor();
        TextChannel textChannel = jda.getTextChannelsByName(plugin.getConfig().getString("discord.channel"), true).get(0);
        String channel = ChatColor.translateAlternateColorCodes('&', "&2[Discord] &r");
        if(event.getChannel().equals(textChannel)){
        if(msg.equals("-players")){
            String pls = " ";
            ArrayList<String> players = new ArrayList<>();
            for(Player p : Bukkit.getOnlinePlayers()){
                players.add(p.getDisplayName());
            }
            for (String s : players) {
                String player = s + " \n";
                pls = pls + player;
            }
            event.getChannel().sendMessage("```" + pls + "```").queue();
            return;
        }
        if(msg.startsWith("-")) {
            msg = msg.substring(1);
            Bukkit.broadcastMessage(channel + user.getName() + ": " + msg);
        }
        }
        }

    }

