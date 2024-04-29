package himcd.heretic.game;

import himcd.heretic.menu.ChoosePowerMenu;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.hereticT;
import static himcd.heretic.TickRunner.chooseMenu;
import static himcd.heretic.TickRunner.prepareTime;
import static himcd.heretic.game.HPlayer.*;
import static himcd.heretic.game.HPlayerInfo.player_info;
import static himcd.heretic.menu.MainMenu.prepared;
import static himcd.heretic.util.Message.*;

public final class GameState {
    public static final List<Location> portal_frame = new ArrayList<>();
    public static final GameRunner gr = new GameRunner();
    public static State state = State.NONE;
    public static int gameTime = 0;
    public static int supplyTime = 0;
    public static BukkitTask game_task;

    public static void reset() {
        var border = Bukkit.getWorld("world").getWorldBorder();
        border.setSize(Integer.MAX_VALUE);
        heretic = null;
        believers.clear();
        hereticT.removeEntries(hereticT.getEntries());
        believerT.removeEntries(believerT.getEntries());
        if (game_task != null && !game_task.isCancelled()) {
            game_task.cancel();
        }
        gameTime = 0;
        supplyTime = 0;
        prepareTime = -1;
        state = State.NONE;
        players.clear();
        prepared.clear();
        portal_frame.clear();
        tasks.forEach(ScheduledTask::cancel);
        tasks.clear();
        chooseMenu = null;
        player_info.clear();
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            p.hideBossBar(bar_h);
            p.hideBossBar(bar_time);
            resetPlayer(p);
            player_info.putIfAbsent(p, new HPlayerInfo("", ""));
        });
        h_board.getEntries().forEach(h_board::resetScores);
    }

    public static void prepare() {
        //选power
        Collections.shuffle(prepared);
        var h = prepared.removeFirst();
        h.openInventory(new ChoosePowerMenu(h).getInventory());
        state = State.PREPARE;
    }

    public static void start(Player h, int power_id) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showBossBar(bar_h);
            p.showBossBar(bar_time);
            p.setGameMode(GameMode.SURVIVAL);
            p.closeInventory();
        });
        var border = h.getWorld().getWorldBorder();
        border.setSize(256);
        //玩家队伍
        heretic = new HPlayer(h, player_info.get(h), power_id);
        believers.addAll(prepared.stream()
                .map(player -> new HPlayer(player, player_info.get(player))).toList());
        //随机传送
        prepared.add(h);
        prepared.forEach(p -> p.addScoreboardTag("spread"));
        var s = Bukkit.getServer();
        s.dispatchCommand(s.getConsoleSender(), "spreadplayers 0 0 100 128 false @a[tag=spread]");
        s.dispatchCommand(s.getConsoleSender(), "kill @e[type=item]");
        prepared.forEach(p -> p.removeScoreboardTag("spread"));
        //变量
        chooseMenu = null;
        state = State.FIRST;
        var r = new Random();
        for (int i = 0; i < 5; i++) {
            var x = r.nextInt(-128, 129);
            var z = r.nextInt(-128, 129);
            portal_frame.add(new Location(h.getWorld(), x, 0, z));
            Objective frame = h_board.getObjective("frame");
            if (frame != null) {
                frame.getScore("%d %d".formatted(x, z)).setScore(i);
            }
        }
        //给物品,信息
        h.setScoreboard(h_board);
        players.values().forEach(HPlayer::init);
    }

    //进入二阶段
    public static void intoSecond() {
        if (state != State.FIRST) return;
        state = State.SECOND;
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hideBossBar(bar_h);
            p.sendMessage(msg.deserialize("<gold>[System] <white>坐标上传完毕,进入二阶段."));
        });
        gameTime = 24001;
    }

    //进入死斗
    public static void intoEnding() {
        if (state != State.SECOND) return;
        state = State.ENDING;
        players.keySet().forEach(p -> p.teleport(new Location(p.getWorld(), -16, 5, -16)));
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hideBossBar(bar_time);
            p.sendMessage(msg.deserialize("<gold>[System] <white>死斗"));
        });
    }

    //结束游戏
    public static void endGame(Player winner) {
        if (winner != null) {
            Bukkit.broadcast(msg.deserialize("<gold>胜利者：<reset>%s".formatted(winner.getName())));
        }
        reset();
    }

    public enum State {
        NONE, FIRST, SECOND, ENDING, PREPARE
    }
}