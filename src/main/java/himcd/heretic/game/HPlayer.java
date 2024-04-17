package himcd.heretic.game;

import himcd.heretic.Heretic;
import himcd.heretic.role.Role;
import himcd.heretic.role.power.Power;
import himcd.heretic.role.skill.Skill;
import himcd.heretic.util.ItemCreator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.hereticT;
import static himcd.heretic.util.Message.msg;


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
        p.getInventory().addItem(ItemCreator.create(Material.CLOCK).name(msg.deserialize("<gold>主菜单")).getItem());
    }

    public void init() {
        role.equip();
        skill.giveItem();
        if (power != null) {
            power.giveItem(player);
        }
        var inv = player.getInventory();
        inv.addItem(new ItemStack(Material.BREAD, 6));
//        for (var m : new Material[]{Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL}) {
//            inv.addItem(ItemCreator.create(m).getItem());
//        }
        for (var m : new Material[]{Material.IRON_AXE}) {
            inv.addItem(ItemCreator.create(m).getItem());
        }
    }
}