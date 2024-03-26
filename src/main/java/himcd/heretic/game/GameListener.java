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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Random;

import static himcd.heretic.Heretic.*;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.heretic;
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
            Bukkit.broadcast(msg.deserialize("<gold>[test]<white>二阶段"));
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler
    void onDeath(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player hurt)) return;
        //检测死亡
        var damage = e.getFinalDamage();
        if (damage < hurt.getHealth()) return;
        e.setCancelled(true);
        //结束判定
        if (hereticT.hasPlayer(hurt)) {
            //H死亡
            Player winner = null;
            var cause = e.getDamageSource().getCausingEntity();
            if (cause instanceof Player) winner = (Player) cause;
            else {
                //todo
                if (cause != null) {
                    var owner = cause.getPersistentDataContainer().get(new NamespacedKey(plugin, "owner"), PersistentDataType.STRING);
                    if (owner != null) {
                        winner = Bukkit.getPlayer(owner);
                    }
                }
            }
            if (winner != null) {
                endGame(winner);
            }
        } else if (believerT.hasPlayer(hurt)) {
            //B死光
            believerT.removePlayer(hurt);
            if (believerT.getSize() == 0) {
                endGame(heretic.player());
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    void onUse(PlayerInteractEvent e) {
        Player user = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR
                || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        var item = e.getItem();
        if (item == null || !item.getItemMeta().hasCustomModelData()) return;
        var id = item.getItemMeta().getCustomModelData();
        switch (id) {
            case 1000000 -> {
                //引力手雷
                e.setCancelled(true);
                item.setAmount(item.getAmount() - 1);
                Vector normalize = user.getLocation().getDirection().normalize();
                if (user.isSneaking()) {
                    normalize.multiply(1).setY(0.1);
                } else {
                    normalize.multiply(2);
                }
                Location location = user.getEyeLocation();
                Item item1 = (Item) user.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
                item1.setItemStack(new ItemStack(Material.IRON_BLOCK));
                item(item1, user);
                item1.setVelocity(normalize);
                new BukkitRunnable() {
                    int t = 0;

                    @Override
                    public void run() {
                        Location location1 = item1.getLocation();
                        if (t >= 10) {
                            user.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location1, 10, 1, 1, 1, null);
                            user.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location1, 500, 0.5, 0.5, 0.5, 0.1, null, true);
                            location1.getNearbyPlayers(3, player -> !player.equals(user) && player.getGameMode() == GameMode.ADVENTURE)
                                    .forEach(player -> {
                                        player.setVelocity(AtoB(location1, player.getLocation()).setY(0.5));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1, true));
                                        player.damage(3, user);
                                    });
                            user.getWorld().playSound(location1, Sound.ENTITY_GENERIC_EXPLODE, .5f, 3f);
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
                                        Location particleLocation = new Location(user.getWorld(), x, location1.getY(), z);
                                        user.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1,
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
                            user.getWorld().spawnParticle(Particle.FLAME, location1, 5, 0.1, 0.1, 0.1, 0.05, null, true);
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
            case 2000000 -> {
                //破片手雷
                e.setCancelled(true);
                item.setAmount(item.getAmount() - 1);
                Vector normalize = user.getLocation().getDirection().normalize();
                if (user.isSneaking()) {
                    normalize.multiply(1).setY(0.5);
                } else {
                    normalize.multiply(2);
                }
                Location location = user.getEyeLocation();
                Item item1 = (Item) user.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
                item1.setItemStack(new ItemStack(Material.IRON_INGOT));
                item(item1, user);
                item1.setVelocity(normalize);
                new BukkitRunnable() {
                    int t = 0;

                    @Override
                    public void run() {
                        Location location1 = item1.getLocation();
                        if (t >= 40) {
                            user.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location1, 10, 1, 1, 1, null);
                            user.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location1, 500, 0.5, 0.5, 0.5, 0.1, null, true);
                            location1.getNearbyPlayers(3, player -> !player.equals(user) && player.getGameMode() == GameMode.ADVENTURE)
                                    .forEach(player -> player.damage(3, user));
                            popian(location1.clone(), user);
                            popian(location1.clone(), user);
                            popian(location1.clone(), user);
                            popian(location1.clone(), user);
                            popian(location1.clone(), user);


                            user.getWorld().playSound(location1, Sound.ENTITY_GENERIC_EXPLODE, .5f, 3f);
                            item1.remove();
                            cancel();
                        } else {
                            t++;
                            item1.customName(msg.deserialize("<gold> %s".formatted(t)));
                            item1.setCustomNameVisible(true);
                            user.getWorld().spawnParticle(Particle.CRIT, location1, t, 0.1, 0.1, 0.1, 0.05 * t, null, true);
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

    private void endGame(Player winner) {
        reset();
    }

    public void popian(Location l, Player p) {
        Random random = new Random();
        double x = l.getX() + (random.nextDouble() - 0.5) * 20;
        double y = l.getY() + Math.abs(random.nextDouble() - 0.5) * 20;
        double z = l.getZ() + (random.nextDouble() - 0.5) * 20;
        Location location = new Location(p.getWorld(), x, y, z);
        Vector vector = AtoB(l, location);
        for (int t = 0; t <= 20; t++) {
            l.add(vector);
            p.getWorld().spawnParticle(Particle.END_ROD, l, 1, 0, 0, 0, 0, null, true);
            double t1 = t * 0.05;
            l.subtract(0, t1, 0);
            l.getNearbyPlayers(3, player -> !player.equals(p) && player.getGameMode() == GameMode.ADVENTURE)
                    .forEach(player -> {
                        player.damage(3, player);
                        l.multiply(0);
                    });
        }

    }
}