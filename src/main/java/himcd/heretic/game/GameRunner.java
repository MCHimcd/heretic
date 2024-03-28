package himcd.heretic.game;

import himcd.heretic.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.msb;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.*;

public final class GameRunner extends BukkitRunnable {
    @Override
    public void run() {
        gameTime++;
        if(gameTime==24000) intoSecond();
        else if(gameTime==36000) {
            players.keySet().forEach(p->p.teleport(new Location(p.getWorld(), -16, 5, -16)));
            Bukkit.broadcast(Message.msg.deserialize("<dark_red>死斗"));
        }
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