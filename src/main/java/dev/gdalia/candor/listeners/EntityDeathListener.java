package dev.gdalia.candor.listeners;

import dev.gdalia.candor.managers.LootTablesManager;
import dev.gdalia.candor.structs.objects.LootItem;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class EntityDeathListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        EntityType entityType = event.getEntity().getType();
        List<LootItem> list = LootTablesManager.getInstance().getLootTable(entityType);
        if (list == null || list.isEmpty()) return;

        event.getDrops().clear();
        setDrop(getChance(list), event.getEntity().getLocation());
    }

    /**
     * Starting with creating a new ItemStack with the giving material & amount,
     * after that will modify the lore to the giving lore same as the enchantments,
     * and then it will set a display name, and it will drop the final item result naturally.
     *
     * @param lootItem the targeted loot box
     * @param location the targeted location
     */
    private void setDrop(LootItem lootItem, Location location) {
        //Creating new ItemStack & Setting a copy of the ItemMeta.
        ItemStack itemStack = new ItemStack(lootItem.getMaterial(), lootItem.getAmount());
        ItemMeta itemMeta = itemStack.getItemMeta();

        //Modifying the data into the ItemMeta.
        itemMeta.lore(lootItem.getLore());
        itemMeta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(lootItem.getDisplayName()));
        lootItem.getEnchantments().forEach((enchantment, integer) -> itemMeta.addEnchant(enchantment, integer, true));

        //Setting the new ItemMeta in the ItemStack & Dropping the ItemStack naturally at the entity death location.
        itemStack.setItemMeta(itemMeta);
        location.getWorld().dropItemNaturally(location, itemStack);
    }

    /**
     * Getting the sum of elements that is in the list,
     * after that getting a random int with the bound of the parameter sum + 1,
     * setting two parameters temp & index with the int of 0,
     * setting a while loop with the check of index is small then the list size,
     * and then modifying the temp to the reachable drop chance that is in the list,
     * after that checking if the temp is higher to equals,
     * if yes it will return the reachable loot table,
     * if no it will add a number to the index,
     * if the while didn't find a loot table, it will return a loot table of the list size - 1.
     *
     * @param list list of the specified loot tables
     * @return a random loot table, but more likely that the loot table with the highest drop chance will be the chosen one.
     */
    private LootItem getChance(List<LootItem> list) {
        int sum = list.stream().mapToInt(LootItem::getDropChance).sum();
        int result = random.nextInt(sum + 1);
        int temp = 0;
        int index = 0;

        while (index < list.size()) {
            temp += list.get(index).getDropChance();
            if (temp >= result) return list.get(index);
            index++;
        }
        return list.get(list.size() - 1);
    }
}
