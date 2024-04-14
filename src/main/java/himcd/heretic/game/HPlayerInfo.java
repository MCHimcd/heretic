package himcd.heretic.game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public record HPlayerInfo(String role, String skill) {
    public static Map<Player, HPlayerInfo> player_info = new HashMap<>();
}