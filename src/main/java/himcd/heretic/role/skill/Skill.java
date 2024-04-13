package himcd.heretic.role.skill;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static himcd.heretic.util.Message.msg;

public abstract class Skill {
    protected Player player;

    public Skill(Player pl) {
        player = pl;
    }

    public static Skill of(Player pl, String name) {
        return switch (name) {
            case "heal" -> new Heal(pl);
            default -> new Heal(pl);
        };
    }

    public static ItemStack getItem(String name) {
        return switch (name) {
            case "heal" -> ItemCreator.create(Material.GOLD_INGOT).name(msg.deserialize("<gold>治疗")).getItem();
            default -> new ItemStack(Material.AIR);
        };
    }

    public abstract void use();
}