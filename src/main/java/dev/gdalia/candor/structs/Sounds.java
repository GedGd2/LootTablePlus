package dev.gdalia.candor.structs;

import dev.gdalia.candor.LootTablePlus;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sounds {

    public static void playSound(CommandSender sender, Sound sound, float volume, float pitch) {
        if (!(sender instanceof Player player)) return;
        if (LootTablePlus.getInstance().getConfig().getBoolean("disable-sounds")) return;
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
