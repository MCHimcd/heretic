package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static himcd.heretic.util.Message.msg;

public abstract class Power {
    public static Power of(int id) {
        return switch (id) {
            case 1, 3 -> new Brave();
            case 2->new Joker();
            default -> new Brave();
        };
    }

    //选择菜单里的物品
    public static ItemStack chooseItem(int i) {
        var it = switch (i) {
            case 1 -> ItemCreator.create(Material.IRON_SWORD).name(msg.deserialize("<red>Brave"));
            case 2 -> ItemCreator.create(Material.SKELETON_SKULL).name(msg.deserialize("<gold>Joker"));
            case 3 -> ItemCreator.create(Material.DIAMOND).name(msg.deserialize("test3"));
            default -> ItemCreator.create(Material.AIR);
        };
        return it.data(i).hideAttributes().getItem();
    }

    public abstract ItemStack getItem();

    //1阶段被动
    public abstract BiConsumer<HPlayer, List<Player>> getBuff1();

    //2阶段被动
    public abstract Consumer<HPlayer> getBuff2();
}