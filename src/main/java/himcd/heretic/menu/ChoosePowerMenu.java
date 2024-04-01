package himcd.heretic.menu;

import himcd.heretic.role.power.Power;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static himcd.heretic.TickRunner.chooseMenu;
import static himcd.heretic.game.GameState.start;
import static himcd.heretic.util.Message.msg;

public class ChoosePowerMenu extends SlotMenu {
    private final List<Integer> ids = new ArrayList<>(IntStream.rangeClosed(1, 3).boxed().toList());
    private int time=300;

    public ChoosePowerMenu(Player player) {
        super(9, msg.deserialize("<gold>选择你的力量"), player);
        chooseMenu = this;
        Collections.shuffle(ids);
        //物品
        BiConsumer<ItemStack, Player> f = (i, p) -> start(p, i.getItemMeta().getCustomModelData());
        setSlot(2, Power.chooseItem(ids.getFirst()), f);
        setSlot(4, Power.chooseItem(ids.get(1)), f);
        setSlot(6, Power.chooseItem(ids.get(2)), f);
    }

    public void tick() {
        player.getOpenInventory().setTitle("剩余时间："+time);
        if(time--==0){
            start(player, ids.get(new Random().nextInt(ids.size())));
        }
    }

}