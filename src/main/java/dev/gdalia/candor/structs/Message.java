package dev.gdalia.candor.structs;

import dev.gdalia.candor.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(fixColor(ConfigManager.getPrefix() + msg));
    }

    public static String fixColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
