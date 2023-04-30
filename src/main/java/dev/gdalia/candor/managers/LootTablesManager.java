package dev.gdalia.candor.managers;

import dev.gdalia.candor.LootTablePlus;
import dev.gdalia.candor.models.ConfigFields;
import dev.gdalia.candor.structs.objects.LootItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTablesManager {

    private static LootTablesManager instance;
    private final List<LootItem> lootItems = new ArrayList<>();

    public LootTablesManager() {
        instance = this;
    }

    public static LootTablesManager getInstance() {
        return instance == null ? new LootTablesManager() : instance;
    }

    /**
     * Getting every specified loot table of targeted mob type to a list.
     *
     * @param entityType the targeted mob type
     * @return specified mob list of loot boxes
     */
    public List<LootItem> getLootTable(EntityType entityType) {
        return lootItems.stream().filter(lootItem -> lootItem.getEntityType().equals(entityType)).toList();
    }

    /**
     * Getting the lootItems and then streaming it to a list of LootItem.
     *
     * @return all the loot-tables
     */
    public List<LootItem> getLootTables() {
        return lootItems.stream().toList();
    }

    /**
     * Getting all the loot tables and setting parameter entityType,
     * after that filtering the loot tables to only one type,
     * getting parameters of the current loot table,
     * creating LootBoxTable and adding it into a list.
     *
     */
    public void loadLootTables() {
        ConfigManager.getMobsSection().getKeys(false).stream().toList().forEach(type -> {
            EntityType entityType = EntityType.valueOf(type.toUpperCase());
            ConfigurationSection section = ConfigManager.getTypeSection(type);
            if (section == null) return;

            section.getKeys(false).forEach(s -> {
                String material = section.getString(s + "." + ConfigFields.LootTableFields.MATERIAL);
                List<Component> lore = new ArrayList<>();
                section.getStringList(s + "." + ConfigFields.LootTableFields.LORE).forEach(line -> lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line)));
                String displayName = section.getString(s + "." + ConfigFields.LootTableFields.DISPLAY_NAME);
                ConfigurationSection enchants = LootTablePlus.getInstance().getConfig().getConfigurationSection("mobs." + type + "." + s + "." + ConfigFields.LootTableFields.ENCHANTMENTS);
                if (enchants == null || material == null || displayName == null) return;

                Map<Enchantment, Integer> enchantments = new HashMap<>();
                enchants.getKeys(false).forEach(enchant -> enchantments.put(Enchantment.getByKey(NamespacedKey.minecraft(enchant)), enchants.getInt(enchant + ".level")));
                LootItem table = new LootItem(s, entityType, Material.valueOf(material.toUpperCase()), section.getInt(s + "." + ConfigFields.LootTableFields.MATERIAL_AMOUNT), lore, displayName, enchantments, section.getInt(s + "." + ConfigFields.LootTableFields.DROP_CHANCE));
                lootItems.add(table);
            });
        });
        Bukkit.getConsoleSender().sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + "&asuccessfully loaded all loot boxes!")));
    }

    /**
     * Starting with checking if type is already exist,
     * if no creating a new one,
     * after that creating a new section for the loot-table,
     * and then setting every parameter in the section & adding loot-table to catch.
     *
     * @param lootItem the targeted loot-item
     */
    public void addLootTable(LootItem lootItem) {
        ConfigurationSection type = ConfigManager.getTypeSection(lootItem.getEntityType().name().toLowerCase()) == null ?
                ConfigManager.getMobsSection().createSection(lootItem.getEntityType().name().toLowerCase())
                : ConfigManager.getTypeSection(lootItem.getEntityType().name().toLowerCase());
        ConfigurationSection section = type.createSection(lootItem.getName());

        section.set(ConfigFields.LootTableFields.MATERIAL, lootItem.getMaterial().name());
        section.set(ConfigFields.LootTableFields.MATERIAL_AMOUNT, lootItem.getAmount());
        section.set(ConfigFields.LootTableFields.LORE, lootItem.getLore());
        section.set(ConfigFields.LootTableFields.DISPLAY_NAME, lootItem.getDisplayName());
        ConfigurationSection enchants = section.createSection(ConfigFields.LootTableFields.ENCHANTMENTS);
        lootItem.getEnchantments().forEach((enchantment, integer) -> enchants.createSection(enchantment.getKey().value().toLowerCase()).set("level", integer));
        section.set(ConfigFields.LootTableFields.DROP_CHANCE, lootItem.getDropChance());

        LootTablePlus.getInstance().saveConfig();
        lootItems.add(lootItem);
    }

    /**
     * Getting the loot-table section from the config,
     * and deleting it from config, also removing the loot-table from catch.
     *
     * @param lootItem the targeted loot-item
     */
    public void deleteLootTable(LootItem lootItem) {
        ConfigurationSection section = ConfigManager.getTypeSection(lootItem.getEntityType().name().toLowerCase());
        section.getKeys(false).stream().filter(s -> lootItem.getName().equals(s)).findFirst().ifPresent(s -> section.set(s, null));
        LootTablePlus.getInstance().saveConfig();
        lootItems.remove(lootItem);
    }
}
