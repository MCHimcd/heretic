package himcd.heretic.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    public static Scoreboard h_board;
    public static final BossBar bar_h = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_10);
    public static final BossBar bar_time = BossBar.bossBar(Component.empty(), 1, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

    public static final MiniMessage msg = MiniMessage.miniMessage();

    public static LinkedList<Component> convertMsg(List<String> sl) {
        return sl.stream().map(msg::deserialize).collect(Collectors.toCollection(LinkedList::new));
    }

    public static void beforeStart(Component msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(msg);
            player.setFoodLevel(20);
        }
    }

    public static Component rMsg(String s){
        return msg.deserialize("<reset>"+s);
    }
}