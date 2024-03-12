package himcd.heretic;

import himcd.heretic.menu.ChoosePowerMenu;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static himcd.heretic.game.GameState.*;

public final class TickRunner extends BukkitRunnable {
    public static int prepareTime = -1;
    public static ChoosePowerMenu chooseMenu;

    @Override
    public void run() {
        if (prepareTime > -1) {
            if (prepareTime-- == 0) prepare();
        }
        if(chooseMenu!=null) chooseMenu.tick();
    }
}