package himcd.heretic.game;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;

import static himcd.heretic.Heretic.*;

public final class GameState {
    public static final ArrayList<HPlayer> believers = new ArrayList<>();
    private static final GameRunner gr = new GameRunner();
    public static State state = State.NONE;
    public static int time = 0;
    public static BukkitTask game_task;
    public static HPlayer heretic;

    public static void reset() {
        heretic = null;
        believers.clear();
        hereticT.removeEntries(hereticT.getEntries());
        believerT.removeEntries(believerT.getEntries());
        if (game_task != null) {
            if (!game_task.isCancelled()) {
                game_task.cancel();
            }
        }
        time = 0;
        state = State.NONE;
    }

    public static void prepare(Collection<Player> players) {
        //TODO
    }

    public static void start() {
        game_task = gr.runTaskTimer(plugin, 0, 1);
        state = State.FIRST;
    }

    public enum State {
        NONE, FIRST, SECOND
    }
}