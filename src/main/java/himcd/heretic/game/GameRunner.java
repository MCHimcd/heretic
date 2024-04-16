package himcd.heretic.game;

import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.heretic;
import static himcd.heretic.game.HPlayer.players;

public final class GameRunner extends BukkitRunnable {
    @Override
    public void run() {
        gameTime++;
        if (gameTime == 24000) intoSecond();
        else if (gameTime == 36000) intoEnding();
        switch (state) {
            // 游戏逻辑
            case FIRST -> {
                var h = heretic.player();
                var ps = h.getWorld().getNearbyPlayers(h.getLocation(), 10)
                        .stream().filter(p -> believerT.hasEntity(p)).toList();
                heretic.power().getBuff1().accept(heretic, ps);
            }
            case SECOND -> heretic.power().getBuff2().accept(heretic);
            case ENDING -> players.keySet().forEach(p -> p.damage(0.1));
        }
    }
}