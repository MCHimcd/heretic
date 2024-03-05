package himcd.heretic.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RoleEquip {
    private final EntityEquipment e;
    private final PlayerInventory inv;

    public static RoleEquip of(Player p) {
        return new RoleEquip(p);
    }

    private RoleEquip(Player p) {
        e = p.getEquipment();
        inv = p.getInventory();
        inv.clear();
    }

    public RoleEquip helmet(ItemStack i) {
        e.setHelmet(i, true);
        return this;
    }

    public RoleEquip chestplate(ItemStack i) {
        e.setChestplate(i, true);
        return this;
    }

    public RoleEquip leggings(ItemStack i) {
        e.setLeggings(i, true);
        return this;
    }

    public RoleEquip boots(ItemStack i) {
        e.setBoots(i, true);
        return this;
    }

    public RoleEquip items(ItemStack... i) {
        inv.addItem(i);
        return this;
    }
}