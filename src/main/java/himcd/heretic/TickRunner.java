package himcd.heretic;

import himcd.heretic.game.HPlayer;
import himcd.heretic.menu.ChoosePowerMenu;
import himcd.heretic.menu.MainMenu;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;

import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.believers;
import static himcd.heretic.game.HPlayer.heretic;
import static himcd.heretic.util.Message.*;

public final class TickRunner extends BukkitRunnable {
    public static int prepareTime = -1;
    public static ChoosePowerMenu chooseMenu;

    @Override
    public void run() {
        if (prepareTime > -1) {
            if (prepareTime-- == 0) prepare();
        }
        if (chooseMenu != null) chooseMenu.tick();
        //信息显示
        switch (state) {
            case NONE -> beforeStart(msg.deserialize("%s %s".formatted(
                    prepareTime == -1 ? "人数过少" : "距离游戏开始：%s".formatted((prepareTime + 20) / 20),
                    Bukkit.getOnlinePlayers().stream().anyMatch(p -> p.getGameMode() == GameMode.ADVENTURE && !MainMenu.prepared.contains(p)) ?
                            "未准备玩家：" + Bukkit.getOnlinePlayers().stream().filter(p -> p.getGameMode() == GameMode.ADVENTURE && !MainMenu.prepared.contains(p)).map(Player::getName).collect(Collectors.joining(","))
                            : ""
            )));
            case FIRST, SECOND -> {
                //BossBar显示
                if (state == State.FIRST) {
                    //1阶段
                    var c = 5 - portal_frame.size();
                    bar_h.progress(c / 5f);
                    bar_h.name(msg.deserialize("<gold>Heretic进度：<aqua>%d<white>/<aqua>5".formatted(c)));
                    bar_h.color(switch (c) {
                        case 0, 1 -> BossBar.Color.GREEN;
                        case 2, 3 -> BossBar.Color.YELLOW;
                        case 4 -> BossBar.Color.RED;
                        default -> BossBar.Color.WHITE;
                    });
                    bar_time.progress((24000 - gameTime) / 24000f);
                    bar_time.name(msg.deserialize("<gold>剩余时间：<aqua>%d".formatted((24000 - gameTime) / 20)));
                    portal_frame.forEach(l -> {
                        heretic.player().spawnParticle(Particle.END_ROD, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 1, 0.5), 0, 0, 10, 0, 1);
                        heretic.player().spawnParticle(Particle.END_ROD, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 1, 0.5), 10, 0, 10, 0, 0);
                        heretic.player().spawnParticle(Particle.TOTEM, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 20, 0.5), 0, 0.5, 0, 0, 5);
                        heretic.player().spawnParticle(Particle.TOTEM, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 20, 0.5), 0, -0.5, 0, 0, 5);
                        heretic.player().spawnParticle(Particle.TOTEM, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 20, 0.5), 0, 0, 0, 0.5, 5);
                        heretic.player().spawnParticle(Particle.TOTEM, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 20, 0.5), 0, 0, 0, -0.5, 5);
                        heretic.player().spawnParticle(Particle.DUST_COLOR_TRANSITION, l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0.5, 20, 0.5), 30, 0.3, 0.3, 0.3, 0.5, new Particle.DustTransition(Color.BLUE, Color.AQUA, 1));
                    });
                } else {
                    //2阶段
                    bar_time.progress((36000 - gameTime) / 12000f);
                    bar_time.name(msg.deserialize("<gold>剩余时间：<aqua>%d".formatted((36000 - gameTime) / 20)));
                }
                //指向H
                var hl = heretic.player().getLocation();
                believers.stream().map(HPlayer::player).forEach(p -> {
                    var pl = p.getLocation();
                    var v1 = pl.getDirection();
                    var v2 = pl.clone().subtract(hl).toVector();
                    var angle = 170 - Math.toDegrees(v2.angle(v1));
                    int v = (v1.crossProduct(v2).getY() >= 0 ? 1 : -1) * (int) Math.min(12, angle / 5);
                    p.sendActionBar(msg.deserialize(
                            "<white>" + "|".repeat(v + 12) + "<gold>|<white>" + "|".repeat(12 - v)
                    ));
                });
            }
        }
    }
}