package himcd.heretic.role.skill;

import himcd.heretic.role.Role;
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
            default -> new Heal(pl);
        };
    }

    public static ItemStack getItem(String name) {
        return switch (name) {
            case "Heal" -> Role.getSkillItem(3000000);
            default -> new ItemStack(Material.AIR);
        };
    }

    public abstract void use();
}