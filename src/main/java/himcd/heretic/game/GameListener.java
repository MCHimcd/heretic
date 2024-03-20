package himcd.heretic.game;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import static himcd.heretic.Heretic.*;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.util.Message.msg;

public final class GameListener implements Listener {
    public static void circle(double r, Player p, Location a) {
        for (double degree = 0; degree < 360; degree++) {
            double rd = Math.toRadians(degree);
            double x = r * Math.sin(rd);
            double z = r * Math.cos(rd);
            a.add(x, 0, z);
            p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, a, 4, 0, 0, 0, 0.02, new Particle.DustTransition(Color.RED, Color.BLUE, 1), true);
            a.subtract(x, 0, z);
        }
    }

    @EventHandler
    void onPlaceBlock(BlockPlaceEvent e) {
        var b = e.getBlockPlaced();
        if (b.getType() != Material.END_PORTAL_FRAME) return;
        var opf = portal_frame.stream().filter(l -> l.getBlockX() == b.getX() && l.getBlockZ() == b.getZ()).findAny();
        if (opf.isEmpty()) return;
        portal_frame.remove(opf.get());
        if (portal_frame.isEmpty()) {
            state = State.SECOND;

            //todo 进入二阶段
        }
    }

    @EventHandler
    void onDeath(PlayerDeathEvent e) {
        var p = e.getPlayer();
        if (hereticT.hasPlayer(p)) {
            //todo 游戏结束
        } else if (believerT.hasPlayer(p)) {
            believerT.removePlayer(p);
            if (believerT.getSize() == 0) {
                //todo 游戏结束
            }
        }
        e.setCancelled(true);
    }

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
                item.setAmount(item.getAmount() - 1);
                Vector normalize = p.getLocation().getDirection().normalize();
                if (p.isSneaking()) {
                    normalize.multiply(1).setY(0.1);
                } else {
                    normalize.multiply(2);
                }
                Location location = p.getEyeLocation();
                Item item1 = (Item) p.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
                item1.setItemStack(new ItemStack(Material.IRON_BLOCK));
                item(item1, p);
                item1.setVelocity(normalize);
                new BukkitRunnable() {
                    int t = 0;

                    @Override
                    public void run() {
                        Location location1 = item1.getLocation();
                        if (t >= 10) {
                            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location1, 10, 1, 1, 1, null);
                            p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location1, 500, 0.5, 0.5, 0.5, 0.1, null, true);
                            location1.getNearbyPlayers(3, player -> !player.equals(p) && player.getGameMode() == GameMode.ADVENTURE)
                                    .forEach(player -> {
                                        player.setVelocity(AtoB(location1, player.getLocation()).setY(0.5));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1, true));
                                        player.damage(3, p);
                                    });
                            p.getWorld().playSound(location1, Sound.ENTITY_GENERIC_EXPLODE, .5f, 3f);
                            new BukkitRunnable() {
                                final int maxR = 0;
                                double radius = 3;

                                @Override
                                public void run() {
                                    if (radius < maxR) {
                                        cancel();
                                        return;
                                    }
                                    for (double theta = 0; theta < Math.PI * 2; theta += Math.PI / 180) {
                                        double x = location1.getX() + radius * Math.cos(theta);
                                        double z = location1.getZ() + radius * Math.sin(theta);
                                        Location particleLocation = new Location(p.getWorld(), x, location1.getY(), z);
                                        p.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1,
                                                0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1f));
                                    }
                                    radius -= 0.6;
                                }
                            }.runTaskTimer(plugin, 0, 1);
                            item1.remove();
                            cancel();
                        } else {
                            t++;
                            item1.customName(msg.deserialize("<gold> %s".formatted(t)));
                            item1.setCustomNameVisible(true);
                            p.getWorld().spawnParticle(Particle.FLAME, location1, 5, 0.1, 0.1, 0.1, 0.05, null, true);
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }

    public void armor_stand(ArmorStand entity) {
        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setVisible(false);
        entity.setMarker(true);
        entity.setSmall(true);
        entity.setHeadPose(EulerAngle.ZERO.setY(-90));
    }

    public void item(Item item, Player player) {
        item.setCanMobPickup(false);
        item.setThrower(player.getUniqueId());
        item.setOwner(player.getUniqueId());
        item.setCanPlayerPickup(false);
    }

    public Vector AtoB(Location A, Location B) {
        return B.clone().subtract(A).toVector().normalize();
    }

    private void endGame(Player winner){
        reset();
    }
}