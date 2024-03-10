package himcd.heretic.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ItemCreator {
    private final ItemStack item;

    private ItemCreator(Material type) {
        item = new ItemStack(type);
        item.editMeta(meta -> {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        });
    }

    public static ItemCreator create(Material type) {
        return new ItemCreator(type);
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemCreator name(Component name) {
        item.editMeta(meta -> meta.displayName(name));
        return this;
    }

    public ItemCreator data(int data) {
        item.editMeta(meta -> meta.setCustomModelData(data));
        return this;
    }

    public ItemCreator lore(Component... lore) {
        item.editMeta(meta -> meta.lore(List.of(lore)));
        return this;
    }

    public ItemCreator enchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemCreator attribute(Attribute attribute, String name, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        item.editMeta(meta -> meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(),name, amount, operation,slot)));
        return this;
    }

    public ItemCreator attribute(Attribute attribute, String name, double amount, EquipmentSlot slot) {
        return attribute(attribute, name, amount, AttributeModifier.Operation.ADD_NUMBER,slot);
    }

    public ItemCreator attribute(Attribute attribute, double amount, EquipmentSlot slot){
        return attribute(attribute, "generic", amount,slot);
    }
}