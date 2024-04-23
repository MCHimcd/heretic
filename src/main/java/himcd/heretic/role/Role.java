package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static himcd.heretic.util.Message.rMsg;

public abstract class Role {
    public final static Map<String, ItemStack> chooseItems = new HashMap<>() {{
        put("Default", ItemCreator.create(Material.IRON_SWORD).name(rMsg("Default")).hideAttributes().getItem());
        put("Peter", ItemCreator.create(Material.IRON_SWORD).name(rMsg("Default")).hideAttributes().getItem());
    }};


    protected final Player p;

    public Role(Player pl) {
        p = pl;
    }

    public static Role of(Player pl, String name) {
        return switch (name) {
            case "Peter" -> new Peter(pl);
            default -> new Default(pl);
        };
    }

    //物品、装备
    abstract public void equip();

    @Override
    public String toString() {
        return "";
    }

    public Player getPlayer() {
        return p;
    }

}