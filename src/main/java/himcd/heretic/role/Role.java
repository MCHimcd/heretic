package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Role {
    protected final Player p;

    public Role(Player pl) {
        p = pl;
    }

    public static Role of(Player pl, String name) {
        return switch (name) {
            case "Default" -> new Default(pl);
            case "Peter" -> new Peter(pl);
            default -> null;
        };
    }

    public static ItemStack getSkillItem(int id) {
        return switch (id) {
            case 1 ->
                    ItemCreator.create(Material.SNOWBALL).name(Message.msg.deserialize("<gold><bold>引力手雷</bold> <red><右键>")).lore(Message.msg.deserialize("""
                                 <gray><引力手雷>
                             <gray>右键可以扔出，蹲下右键可以抛出.
                             <gray>一段时间后<red><bold>爆炸</bold></red>，吸引周围敌人，造成<red><bold>伤害</bold></red>和<red><bold>减速</bold></red>效果\
                            """)).data(1000000).getItem();
            case 2 ->
                    ItemCreator.create(Material.CLAY).name(Message.msg.deserialize("<gold><bold>破片手雷</bold> <red><右键>")).data(2000000).getItem();
            default -> null;
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