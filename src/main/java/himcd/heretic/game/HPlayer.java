package himcd.heretic.game;

import himcd.heretic.Heretic;
import himcd.heretic.role.Role;
import himcd.heretic.role.power.Power;
import himcd.heretic.role.skill.Skill;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.hereticT;


public record HPlayer(Player player, Role role, Skill skill, Power power) {
    public static final ArrayList<HPlayer> believers = new ArrayList<>();
    public static HashMap<Player, HPlayer> players = new HashMap<>();
    public static HPlayer heretic;

    public HPlayer(Player player, HPlayerInfo info) {
        this(player, Role.of(player, info.role()), Skill.of(player, info.skill()), null);
        believerT.addPlayer(player);
    }

    public HPlayer(Player player, HPlayerInfo info, int power) {
        this(player, Role.of(player, info.role()), Skill.of(player, info.skill()), Power.of(power));
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

    public static void resetPlayer(Player p) {
        p.getInventory().clear();
        p.clearActivePotionEffects();
        p.setGameMode(GameMode.ADVENTURE);
        Team team = Heretic.msb.getEntityTeam(p);
        if (team != null) {
            team.removeEntity(p);
        }
        p.removeScoreboardTag("docs");
        HPlayerInfo.player_info.put(p, new HPlayerInfo("Default", "Heal"));
        p.getInventory().addItem(new ItemStack(Material.CLOCK));
    }
}