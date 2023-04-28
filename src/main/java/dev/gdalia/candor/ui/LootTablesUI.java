package dev.gdalia.candor.ui;

import dev.gdalia.candor.managers.LootTablesManager;
import dev.gdalia.candor.structs.Message;
import dev.gdalia.candor.structs.Sounds;
import dev.gdalia.candor.utils.ItemBuilder;
import dev.gdalia.candor.utils.RomanConverter;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Set;

/**
 * In-game deleter allowing server administrators to delete loot-tables.
 */
public class LootTablesUI extends Gui {

    /**
     * Constructs a new Gui instance.
     *
     * @param requester the player deleting the loot-tables
     */
    public LootTablesUI(Player requester) {
        super(6, Message.fixColor("&7LootTables"), Set.of());

        disableAllInteractions();
        GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
        getFiller().fillBorder(GUI_BORDER);

        setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cClose").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player) || !requester.getUniqueId().equals(requester.getUniqueId())) {
                requester.kick(Component.text("&cSorry, but unfortunately we had to kick you from the server due to security reasons."));
                return;
            }

            Sounds.playSound(requester, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            requester.closeInventory();
        }));

        LootTablesManager.getInstance().getLootTables().forEach(lootTable -> {
            ItemBuilder ib = new ItemBuilder(Material.CHEST, "&aLootTable - " + lootTable.getName());
            ib.addLoreLines(
                    "&r ",
                    "&8LootTable Properties:",
                    "&7material: &e" + WordUtils.capitalizeFully(lootTable.getMaterial().name().toLowerCase().replace("_", " ")),
                    "&7amount: &e" + lootTable.getAmount(),
                    "&r ",
                    "&7mob-type: &e" + WordUtils.capitalizeFully(lootTable.getEntityType().name().toLowerCase()),
                    "&7display-name: " + lootTable.getDisplayName(),
                    "&7drop-chance: &e" + lootTable.getDropChance(),
                    "&r ");
            if (!lootTable.getLore().isEmpty()) {
                ib.addLoreLines("&7lore:");
                lootTable.getLore().forEach(component -> ib.addLoreLines("&7- &e" + LegacyComponentSerializer.legacyAmpersand().serialize(component)));
                ib.addLoreLines("&r ");
            }

            if (!lootTable.getEnchantments().isEmpty()) {
                ib.addLoreLines("&7enchantments:");
                lootTable.getEnchantments().forEach((enchantment, integer) -> ib.addLoreLines("&7- &e" + WordUtils.capitalizeFully(enchantment.getKey().value()) + " " + RomanConverter.convert(integer)));
                ib.addLoreLines("&r ");
            }

            ib.addLoreLines("&6&m*&6 Left-Click to delete loot-table");
            addItem(new GuiItem(ib.create(), event -> {
                if (event.getClick() != ClickType.LEFT) return;
                Message.sendMessage(requester, "&aYou have successfully deleted the loot-table " + lootTable.getName() + "!");
                Sounds.playSound(requester, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                LootTablesManager.getInstance().deleteLootTable(lootTable);
                requester.closeInventory();
            }));
        });

        open(requester);
    }
}
