package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.RoleEquip;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;
import static himcd.heretic.util.Message.msg;

public class Chloe extends Role {
    public Chloe(Player pl) {
        super(pl);
        //多段跳
        new BukkitRunnable() {
            int jumpcount = 0;
            int jumpcd = 0;

            @Override
            public void run() {
                if (state == NONE) cancel();
                if (!pl.isOnGround()) {
                    pl.getWorld().spawnParticle(Particle.WHITE_SMOKE, pl.getLocation(), 3, 0, 0, 0, 0);
                    if (pl.isSneaking()) {
                        if (jumpcd == 0) {
                            if (jumpcount > 0) {
                                pl.setVelocity(pl.getLocation().getDirection().normalize().multiply(1.3).setY(0.5));
                                pl.playSound(pl, Sound.ENTITY_HORSE_JUMP, 0.5f, 0.5f);
                                circle(1, pl, pl.getLocation());
                                jumpcount--;
                                jumpcd = 5;
                                pl.setFallDistance(0);
                            }
                        }
                    }
                } else {
                    if (jumpcount <= 1) {
                        if (pl.getFoodLevel() > 10) {
                            pl.setFoodLevel(pl.getFoodLevel() - 1);
                            jumpcount++;
                        }
                    }
                    jumpcd = 8;
                }
                if (jumpcd > 0) jumpcd--;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void circle(double r, Player p, Location a) {
        for (double degree = 0; degree < 360; degree++) {
            double rd = Math.toRadians(degree);
            double x = r * Math.sin(rd);
            double z = r * Math.cos(rd);
            a.add(x, 0, z);
            p.spawnParticle(Particle.CLOUD, a, 1, 0, 0, 0, 0.02, null);
            a.subtract(x, 0, z);
        }
    }

    @Override
    public void equip() {
        RoleEquip.of(p)
                .helmet(ItemCreator.create(Material.WITHER_SKELETON_SKULL).name(msg.deserialize("<gold>克洛伊"))
                        .attribute(Attribute.GENERIC_ARMOR, 4, EquipmentSlot.HEAD)
                        .getItem())
                .boots(ItemCreator.create(Material.LEATHER_BOOTS).color(Color.WHITE).getItem())
                .chestplate(ItemCreator.create(Material.LEATHER_CHESTPLATE).getItem())
                .leggings(ItemCreator.create(Material.LEATHER_LEGGINGS).getItem())
                .items(ItemCreator.create(Material.WOODEN_SWORD).name(msg.deserialize("<gold><bold>克洛伊<reset>圣剑"))
                                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, 7, EquipmentSlot.HAND)
                                .attribute(Attribute.GENERIC_ATTACK_SPEED, -1.2, EquipmentSlot.HAND)
                                .hideAttributes()
                                .getItem(),
                        ItemCreator.create(Material.COOKIE).name(msg.deserialize("<gray> 压缩饼干")).data(101).getItem());
    }
}