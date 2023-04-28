package dev.gdalia.candor.commands;

import dev.gdalia.candor.managers.LootTablesManager;
import dev.gdalia.candor.structs.Message;
import dev.gdalia.candor.structs.Sounds;
import dev.gdalia.candor.structs.objects.LootItem;
import dev.gdalia.candor.ui.LootTablesUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

public class LootTablesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Message.sendMessage(sender, "&cThis command can only be used by players.");
            return false;
        }

        if (args.length == 0) {
            Message.sendMessage(player, "&7/loottable &6{&edelete&8/&ecreate&6}");
            Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            return false;
        }

        switch (args[0]) {
            case "create" -> {
                if (args.length <= 3) {
                    Message.sendMessage(player, "&7/loottable create &6{&ename&8,&e entity-type&8,&e drop-chance&8&6}");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                    return false;
                }

                if (LootTablesManager.getInstance().getLootTables().stream().anyMatch(lootItem -> lootItem.getName().equals(args[1]))) {
                    Message.sendMessage(player, "&cThe name " + args[1] + " is already used, please try again.");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    return false;
                }

                if (Arrays.stream(EntityType.values()).noneMatch(type -> type.name().equals(args[2].toUpperCase()))) {
                    Message.sendMessage(player, "&cThe mob " + args[2] + " does not exist.");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    return false;
                }

                if (!StringUtils.isNumeric(args[3])) {
                    Message.sendMessage(player, "&cThe drop chance can't contains text.");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    return false;
                }

                if (Integer.parseInt(args[3]) > 100) {
                    Message.sendMessage(player, "&cThe maximum drop chance is 100.");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    return false;
                }

                if (player.getInventory().getItemInMainHand().getType().isEmpty()) {
                    Message.sendMessage(player, "&eYou must hold an item in your hand.");
                    Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                    return false;
                }

                String name = args[1];
                EntityType entityType = EntityType.valueOf(args[2].toUpperCase());
                int dropChance = Integer.parseInt(args[3]);
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Component displayName = itemStack.getItemMeta().displayName();
                List<Component> lore = itemStack.getItemMeta().lore();
                LootItem lootItem = new LootItem(
                        name, entityType, itemStack.getType(),
                        itemStack.getAmount(), lore == null ? new ArrayList<>() : lore,
                        displayName == null ? LegacyComponentSerializer.legacyAmpersand().serialize(itemStack.displayName()) : LegacyComponentSerializer.legacyAmpersand().serialize(displayName),
                        itemStack.getItemMeta().getEnchants(), dropChance);

                LootTablesManager.getInstance().addLootTable(lootItem);
                Message.sendMessage(player, "&aYou have successfully created a loot box.");
            }

            case "delete" -> {
                new LootTablesUI(player);
                Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            }
        }

        return true;
    }
}
