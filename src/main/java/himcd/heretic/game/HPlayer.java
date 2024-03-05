package himcd.heretic.game;

import himcd.heretic.role.Role;
import himcd.heretic.role.power.Power;
import himcd.heretic.role.skill.Skill;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.hereticT;


public record HPlayer(Player player, Role role, Skill skill, Power power) {
    public static HashMap<Player, HPlayer> players = new HashMap<>();

    public HPlayer(Player player, Role role, Skill skill) {
        this(player, role, skill, null);
        believerT.addPlayer(player);
    }

    public HPlayer {
        if (power != null) {
            hereticT.addPlayer(player);
        }
        players.put(player, this);
    }

    public static HPlayer getPlayer(Player player) {
        return players.get(player);
    }

    public static List<HPlayer> getPlayers() {
        return players.values().stream().toList();
    }
}