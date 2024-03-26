package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import himcd.heretic.role.skill.Skill;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Power {
    public static Power of(int id) {
        return null;
    }

    //选择菜单里的物品
    public static ItemStack chooseItem(int i){
        return null;
    }

    //1阶段被动
    public abstract BiConsumer<HPlayer, List<Player>> getBuff1();

    //2阶段被动
    public abstract Consumer<HPlayer> getBuff2();
}