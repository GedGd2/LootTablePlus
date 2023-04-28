package dev.gdalia.candor.structs.objects;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record LootItem(@Getter @NotNull String name, @Getter @NotNull EntityType entityType, @Getter @NotNull Material material, @Getter int amount,
                       @Getter @NotNull List<Component> lore, @Getter @NotNull String displayName,
                       @Getter @NotNull Map<Enchantment, Integer> enchantments, @Getter int dropChance) {

    @Override
    public String toString() {
        return "LootItem{" +
                "name='" + name + '\'' +
                ", entityType=" + entityType +
                ", material=" + material +
                ", amount=" + amount +
                ", lore=" + lore +
                ", displayName='" + displayName + '\'' +
                ", enchantments=" + enchantments +
                ", dropChance=" + dropChance +
                '}';
    }
}
