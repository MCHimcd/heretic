package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static himcd.heretic.util.Message.msg;

public abstract class Role {
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

    public static ItemStack getSkillItem(int id) {
        return switch (id) {
            case 1000000 ->
                    ItemCreator.create(Material.SNOWBALL).name(Message.msg.deserialize("<gold><bold>引力手雷</bold> <red><右键>")).lore(Message.msg.deserialize("""
                                 <gray><引力手雷>
                             <gray>右键可以扔出，蹲下右键可以抛出.
                             <gray>一段时间后<red><bold>爆炸</bold></red>，吸引周围敌人，造成<red><bold>伤害</bold></red>和<red><bold>减速</bold></red>效果\
                            """)).data(1000000).getItem();
            case 2000000 ->
                    ItemCreator.create(Material.CLAY_BALL).name(Message.msg.deserialize("<gold><bold>破片手雷</bold> <red><右键>")).data(2000000).getItem();
            case 2000001 ->ItemCreator.create(Material.REDSTONE).name(Message.msg.deserialize("<red>生命检测仪")).data(2000001).getItem();
            case 3000000 ->ItemCreator.create(Material.GOLD_INGOT).name(msg.deserialize("<gold>治疗")).data(3000000).getItem();
            default -> new ItemStack(Material.AIR);
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