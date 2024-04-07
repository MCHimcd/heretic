package himcd.heretic;

import himcd.heretic.game.HPlayer;
import himcd.heretic.menu.ChoosePowerMenu;
import himcd.heretic.menu.MainMenu;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;

import static himcd.heretic.game.GameState.*;
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
            case NONE -> send_actionbar(msg.deserialize("距离游戏开始：%s 未准备玩家：%s".formatted(
                    prepareTime,
                    MainMenu.prepared.stream().map(Player::getName).collect(Collectors.joining(","))
            )));
            case FIRST, SECOND -> {
                //BossBar显示
                if (state == State.FIRST) {
                    //1阶段
                    var c = portal_frame.size();
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
                    portal_frame.forEach(l-> HPlayer.heretic.player().spawnParticle(Particle.END_ROD,l.getWorld().getHighestBlockAt(l).getLocation().clone().add(0,1,0),1));
                } else {
                    //2阶段
                    bar_time.progress((36000 - gameTime) / 36000f);
                    bar_time.name(msg.deserialize("<gold>剩余时间：<aqua>%d".formatted((36000 - gameTime) / 20)));
                }
                //todo 指向H
            }
        }
    }
}