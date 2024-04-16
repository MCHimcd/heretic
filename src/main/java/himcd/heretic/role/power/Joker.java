package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static himcd.heretic.util.Message.msg;

public class Joker extends Power {

    @Override
    public void giveItem(Player p) {
        p.getInventory().addItem(
                ItemCreator.create(Material.REDSTONE).name(msg.deserialize("<red>生命检测仪")).data(2000001).getItem()
        );
    }

    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return (hPlayer, players) -> {

        };
    }

    @Override
    public Consumer<HPlayer> getBuff2() {
        return hPlayer -> {

        };
    }
}