package dev.gdalia.candor.managers;

import dev.gdalia.candor.LootTablePlus;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigManager {

    /**
     * Getting from config the string prefix.
     *
     * @return the string prefix
     */
    public static String getPrefix() {
        return LootTablePlus.getInstance().getConfig().getString("prefix");
    }

    /**
     * Getting from config mobs section.
     *
     * @return the section mobs from config
     */
    public static ConfigurationSection getMobsSection() {
        return LootTablePlus.getInstance().getConfig().getConfigurationSection("mobs");
    }

    /**
     * Getting a type form the mobs config section.
     *
     * @param type the targeted type
     * @return a type section from the config
     */
    public static ConfigurationSection getTypeSection(String type) {
        return LootTablePlus.getInstance().getConfig().getConfigurationSection("mobs." + type);
    }
}
