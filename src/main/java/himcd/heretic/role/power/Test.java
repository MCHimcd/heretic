package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Test extends Power{
    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return null;
    }

    @Override
    public Consumer<HPlayer> getBuff2() {
        return null;
    }
}