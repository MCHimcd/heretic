package himcd.heretic;

import himcd.heretic.menu.ChoosePowerMenu;
import himcd.heretic.menu.MainMenu;
import net.kyori.adventure.bossbar.BossBar;
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
                //BossBar显示进度
                if (state == State.FIRST) {
                    var c = portal_frame.size();
                    bar.progress(c / 5f);
                    bar.name(msg.deserialize("<gold>Heretic进度：<aqua>%s<white>/<aqua>5".formatted(c)));
                    bar.color(switch (c) {
                        case 0, 1 -> BossBar.Color.GREEN;
                        case 2, 3 -> BossBar.Color.YELLOW;
                        case 4 -> BossBar.Color.RED;
                        default -> BossBar.Color.WHITE;
                    });
                }
                //todo 指向H , frame位置
            }
        }
    }
}