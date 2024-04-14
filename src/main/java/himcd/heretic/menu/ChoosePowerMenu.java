package himcd.heretic.menu;

import himcd.heretic.role.power.Power;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static himcd.heretic.Heretic.logger;
import static himcd.heretic.TickRunner.chooseMenu;
import static himcd.heretic.game.GameState.start;
import static himcd.heretic.util.Message.msg;

public class ChoosePowerMenu extends SlotMenu {
    private final List<Integer> ids = new ArrayList<>(IntStream.rangeClosed(1, 3).boxed().toList());
    private int time = 300;

    public ChoosePowerMenu(Player player) {
        super(9, msg.deserialize("<gold>选择你的力量"), player);
        chooseMenu = this;
        Collections.shuffle(ids);
        //物品
        //todo 点了没反应？？？？？？？？？？？？
        setSlot(1,new ItemStack(Material.ACACIA_BOAT),(i,p)->{logger.info("????????????????????");});
        setSlot(2, Power.chooseItem(ids.getFirst()), (i, p) -> {logger.info("????????????????????");});
        setSlot(4, Power.chooseItem(ids.get(1)), (i, p) -> start(p, ids.get(1)));
        setSlot(6, Power.chooseItem(ids.get(2)), (i, p) -> start(p, ids.get(2)));
    }

    public void tick() {
        getInventory().getViewers()
                .forEach(player -> player.getOpenInventory().setTitle("剩余时间：%s".formatted((time + 20) / 20)));
        if (time-- == 0) {
            randomStart();
        }
    }

    public void randomStart() {
        start(player, ids.get(new Random().nextInt(ids.size())));
    }

}