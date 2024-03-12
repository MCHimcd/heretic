package himcd.heretic.game;

import himcd.heretic.menu.ChoosePowerMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;

import static himcd.heretic.Heretic.*;
import static himcd.heretic.TickRunner.chooseMenu;
import static himcd.heretic.TickRunner.prepareTime;
import static himcd.heretic.game.HPlayer.*;
import static himcd.heretic.menu.MainMenu.prepared;

public final class GameState {
    private static final GameRunner gr = new GameRunner();
    public static State state = State.NONE;
    public static int gameTime = 0;
    public static BukkitTask game_task;

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
        gameTime = 0;
        prepareTime = -1;
        state = State.NONE;
        players.clear();
        prepared.clear();
        chooseMenu = null;
    }

    public static void prepare() {
        //选power
        Collections.shuffle(prepared);
        var h = prepared.removeFirst();
        h.openInventory(new ChoosePowerMenu(h).getInventory());
        state = State.PREPARE;
    }

    public static void start(Player h, int power) {
        //玩家
        heretic = new HPlayer(h, player_info.get(h), power);
        believers.addAll(prepared.stream()
                .map(player -> new HPlayer(player, player_info.get(player))).toList());
        //变量
        chooseMenu = null;
        game_task = gr.runTaskTimer(plugin, 0, 1);
        state = State.FIRST;
    }

    public enum State {
        NONE, FIRST, SECOND, PREPARE
    }
}