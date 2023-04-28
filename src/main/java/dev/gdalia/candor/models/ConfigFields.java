package dev.gdalia.candor.models;

public class ConfigFields {

    public static class LootTableFields {

        public static final String
                MATERIAL = "material", //String
                MATERIAL_AMOUNT = "material-amount", //INTEGER
                LORE = "lore", //LIST<STRING>
                DISPLAY_NAME = "display-name", //STRING
                ENCHANTMENTS = "enchantments", //CONFIGURATION-SECTION
                DROP_CHANCE = "drop-chance"; //INTEGER
        }
}