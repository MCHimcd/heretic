package himcd.heretic.game;

import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.*;

public final class GameRunner extends BukkitRunnable {
    @Override
    public void run() {
        gameTime++;
        switch (state) {
            // 游戏逻辑
            case FIRST -> {
                var h = heretic.player();
                var ps = h.getWorld().getNearbyPlayers(h.getLocation(), 10)
                        .stream().filter(p -> believerT.hasEntity(p)).toList();
                heretic.power().getBuff1().accept(heretic, ps);
            }
            case SECOND -> {
                heretic.power().getBuff2().accept(heretic);
            }
        }
    }
}