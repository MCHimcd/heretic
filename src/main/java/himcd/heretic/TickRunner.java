package himcd.heretic;

import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.game.GameState.*;

public final class TickRunner extends BukkitRunnable {
    public static int prepareTime = -1;
    @Override
    public void run() {
        if(prepareTime>-1){
            if(prepareTime--==0) start();
        }
    }
}