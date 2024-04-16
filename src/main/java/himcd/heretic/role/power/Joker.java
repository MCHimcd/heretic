package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import himcd.heretic.role.Role;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Joker extends Power{
    boolean Given=false;
    boolean Given1=false;
    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return (hPlayer, players) -> {
            if (!Given){
                hPlayer.player().getInventory().addItem(Role.getSkillItem(2000001));

            }
        };
    }

    @Override
    public Consumer<HPlayer> getBuff2() {
        return hPlayer -> {

        };
    }
}
