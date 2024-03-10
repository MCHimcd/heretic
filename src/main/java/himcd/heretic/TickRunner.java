package himcd.heretic;

import himcd.heretic.menu.ChoosePowerMenu;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static himcd.heretic.game.GameState.*;

public final class TickRunner extends BukkitRunnable {
    public static int prepareTime = -1;
    public static ChoosePowerMenu chooseMenu;
    public static int chooseTime = -1;

    @Override
    public void run() {
        if (prepareTime > -1) {
            if (prepareTime-- == 0) prepare();
        }
        if (chooseTime > -1) {
            if (chooseTime-- == 0) {
                chooseMenu.randomStart();
            }
        }
    }
}