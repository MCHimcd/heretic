package himcd.heretic.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

import static himcd.heretic.Heretic.plugin;

public abstract class SlotMenu implements InventoryHolder {
    private final Inventory inventory;
    private final HashMap<Integer, Consumer<ItemStack>> slotFunctions = new HashMap<>();

    public SlotMenu(int size, Component title) {
        inventory = plugin.getServer().createInventory(this, size, title);
    }

    public void setSlot(int slot, ItemStack item, Consumer<ItemStack> function) {
        inventory.setItem(slot, item);
        slotFunctions.put(slot, function);
    }

    public void removeSlot(int slot) {
        inventory.setItem(slot, null);
        slotFunctions.remove(slot);
    }

    public void handleClick(int slot) {
        if (slotFunctions.containsKey(slot)) {
            slotFunctions.get(slot).accept(inventory.getItem(slot));
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}