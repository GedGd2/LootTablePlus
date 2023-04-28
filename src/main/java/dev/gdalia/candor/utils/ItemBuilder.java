package dev.gdalia.candor.utils;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemstack;

    public ItemBuilder(Material type, int amount) {
        this.itemstack = new ItemStack(type, amount);
    }

    public ItemBuilder(Material type, short durability) {
        this.itemstack = new ItemStack(type, 1, durability);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemstack = itemStack;
    }

    public ItemBuilder(Material type, String displayName) {
        this.itemstack = new ItemStack(type);
        ItemMeta itemMeta = itemstack.getItemMeta();
        itemMeta.setDisplayName(fixColor(displayName));
        this.itemstack.setItemMeta(itemMeta);
    }

    public ItemBuilder addLoreLines(String... lines) {
        ItemMeta itemMeta = this.itemstack.getItemMeta();
        List<String> loreList = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        Arrays.stream(lines).forEachOrdered(lore -> {
            assert loreList != null;
            loreList.add(fixColor("&7" + lore));
        });

        itemMeta.setLore(loreList);
        this.itemstack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setType(Material type) {
        this.itemstack.setType(type);
        return this;
    }

    public ItemBuilder setDisplayName(String newDisplayName) {
        ItemMeta itemMeta = this.itemstack.getItemMeta();
        itemMeta.displayName(Component.text(fixColor(newDisplayName)));
        this.itemstack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemstack.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        List<Component> newLore = new ArrayList<>();
        lore.forEach(string -> newLore.add(Component.text(fixColor(string))));
        ItemMeta im = this.itemstack.getItemMeta();
        im.lore(newLore);
        this.itemstack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLoreLine(String newLine, int lineToReplace) {
        List<Component> currentLore = itemstack.lore();
        assert currentLore != null;

        currentLore.set(lineToReplace, Component.text(fixColor(newLine)));
        ItemMeta itemMeta = itemstack.getItemMeta();
        itemMeta.lore(currentLore);
        itemstack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemstack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.itemstack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        this.itemstack.getItemMeta().addItemFlags(flag);
        return this;
    }

    @SneakyThrows
    public ItemBuilder setArmourColor(Color color) {
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemstack.getItemMeta();
        itemMeta.setColor(color);
        this.itemstack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setCustomModelData(int dataModel) {
        ItemMeta im = itemstack.getItemMeta();
        im.setCustomModelData(dataModel);
        itemstack.setItemMeta(im);
        return this;
    }

    @SneakyThrows
    public ItemBuilder setPlayerSkull(OfflinePlayer value) {
        itemstack.setType(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) itemstack.getItemMeta();
        itemMeta.setOwningPlayer(value);
        itemstack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack create() {
        return itemstack;
    }

    private String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
