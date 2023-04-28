package dev.gdalia.candor;

import dev.gdalia.candor.commands.LootTablesCommand;
import dev.gdalia.candor.listeners.EntityDeathListener;
import dev.gdalia.candor.managers.LootTablesManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LootTablePlus extends JavaPlugin {

    @Getter
    private static LootTablePlus instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        LootTablesManager.getInstance().loadLootTables();
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        getCommand("loottables").setExecutor(new LootTablesCommand());

        Bukkit.getConsoleSender().sendMessage(
                Component.text("LootTablePlus is now enabled!")
                        .color(NamedTextColor.AQUA)
                        .decorate(TextDecoration.ITALIC));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                Component.text("LootTablePlus is now disabled!")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.ITALIC));
    }
}
