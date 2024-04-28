package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import himcd.heretic.util.RoleEquip;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;
import static himcd.heretic.util.Message.msg;

public class Default extends Role{
    public Default(Player pl) {
        super(pl);
    }

    @Override
    public void equip() {
        RoleEquip.of(p)
                .helmet(ItemCreator.create(Material.IRON_HELMET).getItem())
                .chestplate(ItemCreator.create(Material.IRON_CHESTPLATE).getItem())
                .leggings(ItemCreator.create(Material.IRON_LEGGINGS).getItem())
                .boots(ItemCreator.create(Material.IRON_BOOTS).getItem())
                .items(ItemCreator.create(Material.IRON_SWORD).getItem());
    }
}