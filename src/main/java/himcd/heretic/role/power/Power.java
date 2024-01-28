package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Power {
    public abstract BiConsumer<HPlayer, List<Player>> getBuff1();
    public abstract Consumer<HPlayer> getBuff2();
}