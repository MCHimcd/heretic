package himcd.heretic;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static himcd.heretic.Heretic.plugin;

public final class GameListener implements Listener {
    @EventHandler
    void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR
                || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        var item = e.getItem();
        if (item == null || !item.getItemMeta().hasCustomModelData()) return;
        var id = item.getItemMeta().getCustomModelData();
        switch (id) {
            case 1000000 -> {
                Boolean Sneaking;
                Vector normalize = p.getLocation().getDirection().normalize();
                if (p.isSneaking()){
                    Sneaking=Boolean.TRUE;
                    normalize.multiply(1);
                }else {
                    Sneaking=Boolean.FALSE;
                    normalize.multiply(1.6);
                }
                Location location = p.getEyeLocation();
                ArmorStand entity = (ArmorStand) p.getWorld().spawnEntity(location.clone().add(0,-1,0), EntityType.ARMOR_STAND);
                entity.getEquipment().setHelmet(new ItemStack(Material.IRON_BLOCK));
                armorstand(entity);
                    new BukkitRunnable() {
                        int t = 0;
                        @Override
                        public void run() {
                            if (t >= 20) {
                                p.getWorld().spawnParticle(Particle.FLAME, location, 100, 0, 0, 0, 0.1, null, true);
                                location.getNearbyPlayers(3, player -> !player.equals(p)&&player.getGameMode()== GameMode.ADVENTURE)
                                        .forEach(player -> {
                                            p.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2f, 1f);
                                            player.setVelocity(AtoB(player.getLocation(),location).setY(0.5));
                                        });
                                new BukkitRunnable() {
                                    double radius = 3;
                                    final int maxR = 0;
                                    @Override
                                    public void run() {
                                        if (radius < maxR) {
                                            cancel();
                                            return;
                                        }
                                        for (double theta = 0; theta < Math.PI * 2; theta += Math.PI / 180) {
                                            double x = location.getX() + radius * Math.cos(theta);
                                            double z = location.getZ() + radius * Math.sin(theta);
                                            Location particleLocation = new Location(p.getWorld(), x, location.getY(), z);

                                            p.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1,
                                                    0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
                                        }
                                        radius -= 0.6;
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                                entity.remove();
                                cancel();
                            }else {
                                entity.teleport(location.clone().add(0,-1,0));
                                t++;
                                location.add(normalize);
                                if (Sneaking){
                                    location.add(0,-0.2*t,0);
                                }else {
                                    location.add(0,-0.1*t,0);
                                }
                                p.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0, null, true);
                            }
                            if (location.getBlock().getType() != Material.AIR){
                                t=20;
                            };
                        }
                    }.runTaskTimer(plugin, 0, 1);
            }
        }
    }
    public void armorstand(ArmorStand entity){
        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setVisible(false);
        entity.setMarker(true);
        entity.setSmall(true);
        entity.setHeadPose(EulerAngle.ZERO.setY(-90));
    }
    public Vector AtoB(Location A , Location B){
        return B.clone().subtract(A).toVector().normalize();
    }
    public static void circle(double r, Player p, Location a) {
        for (double degree = 0; degree < 360 ; degree++) {
            double rd = Math.toRadians(degree);
            double x = r * Math.sin(rd);
            double z = r * Math.cos(rd);
            a.add(x, 0, z);
            p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, a, 4, 0, 0, 0, 0.02, new Particle.DustTransition(Color.RED, Color.BLUE, 1), true);
            a.subtract(x, 0, z);
        }
    }
}