package himcd.heretic.game;

import himcd.heretic.role.Role;
import himcd.heretic.role.power.Power;
import org.bukkit.entity.Player;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.hereticT;

public record HPlayer(Player player, Role role, Power power) {
    public HPlayer(Player player, Role role) {
        this(player, role, null);
        believerT.addPlayer(player);
    }

    public HPlayer {
        if (power != null) {
            hereticT.addPlayer(player);
        }
    }
}