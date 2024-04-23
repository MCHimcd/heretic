package himcd.heretic.role.skill;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static himcd.heretic.util.Message.rMsg;

public abstract class Skill {
    public final static Map<String, ItemStack> chooseItems = new HashMap<>() {{
        put("Heal", ItemCreator.create(Material.GOLD_INGOT).name(rMsg("<gold>治疗")).getItem());
    }};

    protected Player player;

    public Skill(Player pl) {
        player = pl;
    }

    public static Skill of(Player pl, String name) {
        return switch (name) {
            default -> new Heal(pl);
        };
    }

    public abstract void giveItem();

}