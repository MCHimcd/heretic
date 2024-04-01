package himcd.heretic.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    public static final BossBar bar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_10);

    public static final MiniMessage msg = MiniMessage.miniMessage();

    public static LinkedList<Component> convertMsg(List<String> sl) {
        return sl.stream().map(msg::deserialize).collect(Collectors.toCollection(LinkedList::new));
    }

    public static void send_actionbar(Component msg) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(msg));
    }
}