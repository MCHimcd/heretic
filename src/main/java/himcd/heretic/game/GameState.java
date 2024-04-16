package himcd.heretic.game;

import himcd.heretic.menu.ChoosePowerMenu;
import himcd.heretic.role.skill.Skill;
import himcd.heretic.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static himcd.heretic.Heretic.*;
import static himcd.heretic.TickRunner.chooseMenu;
import static himcd.heretic.TickRunner.prepareTime;
import static himcd.heretic.game.HPlayer.*;
import static himcd.heretic.game.HPlayerInfo.player_info;
import static himcd.heretic.menu.MainMenu.prepared;
import static himcd.heretic.util.Message.*;

public final class GameState {
    public static final List<Location> portal_frame = new ArrayList<>();
    private static final GameRunner gr = new GameRunner();
    public static State state = State.NONE;
    public static int gameTime = 0;
    public static BukkitTask game_task;

    public static void reset() {
        heretic = null;
        believers.clear();
        hereticT.removeEntries(hereticT.getEntries());
        believerT.removeEntries(believerT.getEntries());
        if (game_task != null && !game_task.isCancelled()) {
            game_task.cancel();
        }
        gameTime = 0;
        prepareTime = -1;
        state = State.NONE;
        players.clear();
        prepared.clear();
        portal_frame.clear();
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

    public static void start(Player h, int power) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showBossBar(bar_h);
            p.showBossBar(bar_time);
            p.setGameMode(GameMode.SURVIVAL);
            p.closeInventory();
        });
        //玩家队伍
        heretic = new HPlayer(h, player_info.get(h), power);
        believers.addAll(prepared.stream()
                .map(player -> new HPlayer(player, player_info.get(player))).toList());
        //随机传送
        prepared.add(h);
        prepared.forEach(p -> p.addScoreboardTag("spread"));
        var s = Bukkit.getServer();
        s.dispatchCommand(s.getConsoleSender(), "spreadplayers 128 128 100 128 false @a[tag=spread]");
        prepared.forEach(p -> p.removeScoreboardTag("spread"));
        //变量
        chooseMenu = null;
        game_task = gr.runTaskTimer(plugin, 0, 1);
        state = State.FIRST;
        var r = new Random();
        for (int i = 0; i < 5; i++) {
            var x = r.nextInt(256);
            var z = r.nextInt(256);
            portal_frame.add(new Location(h.getWorld(), x, 0, z));
            Objective frame = h_board.getObjective("frame");
            if (frame != null) {
                frame.getScore("%d %d".formatted(x, z)).setScore(i);
            }
        }
        //给物品,信息
        h.setScoreboard(h_board);
        h.getInventory().addItem(ItemCreator.create(Material.END_PORTAL_FRAME).amount(5).getItem());
        players.forEach((p, hp) -> {
            hp.role().equip();
            var inv = p.getInventory();
            inv.addItem(Skill.getItem(player_info.get(p).skill()), new ItemStack(Material.BREAD, 6));
            for (var m : new Material[]{Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL}) {
                inv.addItem(ItemCreator.create(m).getItem());
            }
        });
    }

    //进入二阶段
    public static void intoSecond() {
        if (state != State.FIRST) return;
        state = State.SECOND;
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hideBossBar(bar_h);
            p.sendMessage(msg.deserialize("<gold>[test]<white>二阶段"));
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
            p.sendMessage(msg.deserialize("<gold>[test]<white>死斗"));
        });
    }

    public enum State {
        NONE, FIRST, SECOND, ENDING, PREPARE
    }
}