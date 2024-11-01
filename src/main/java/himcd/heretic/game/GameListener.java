package himcd.heretic.game;

import himcd.heretic.role.power.Joker;
import himcd.heretic.role.power.Power;
import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;

import static himcd.heretic.Heretic.*;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.heretic;
import static himcd.heretic.game.HPlayer.tasks;
import static himcd.heretic.util.Message.*;

public final class GameListener implements Listener {
    public static HashMap<String, ItemStack> items = new HashMap<>() {{
        put("1", ItemCreator.create(Material.SNOWBALL).name(msg.deserialize("<aqua> 冰冻摔炮")).data(5000000).getItem());
        put("2", ItemCreator.create(Material.COOKIE).name(msg.deserialize("<gray> 压缩饼干")).data(101).getItem());

    }};
    public static Location kylelocation = null;
    private final Set<Material> tools = new HashSet<>(Arrays.asList(
            Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
            Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
            Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE,
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD
    ));

    public static void circle(double r, Player p, Location a) {
        for (double degree = 0; degree < 360; degree++) {
            double rd = Math.toRadians(degree);
            double x = r * Math.sin(rd);
            double z = r * Math.cos(rd);
            a.add(x, 0, z);
            p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, a, 1, 0, 0, 0, 0.02, new Particle.DustTransition(Color.WHITE, Color.AQUA, .5f), true);
            a.subtract(x, 0, z);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler
    static void onDeath(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player hurt)) return;
        //检测死亡
        var damage = e.getFinalDamage();
        if (damage < hurt.getHealth()) return;
        e.setCancelled(true);
        if (state == State.NONE || state == State.PREPARE) return;
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
            hurt.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcast(rMsg("%s被%s杀死了".formatted(hurt.getName(), heretic.player().getName())));
            //B死光
            believerT.removePlayer(hurt);
            if (believerT.getSize() == 0) {
                endGame(heretic.player());
            }
        }
        e.setCancelled(true);
    }

    public static void kyle(PlayerInteractEvent e, ItemStack item, Player user) {
        e.setCancelled(true);
        item.setAmount(item.getAmount() - 1);
        kylelocation = user.getLocation();
        kyletower(kylelocation, user);
    }

    public static void kyletower(Location location, Player user) {
    }

    private static void heal(Player user, ItemStack item) {
        //治疗
        double health = user.getHealth();
        double max = Objects.requireNonNull(user.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        double a = max - health;
        if (a >= 8) {
            item.setAmount(item.getAmount() - 1);
            user.setHealth(health + 8);
            user.playSound(user, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            user.spawnParticle(Particle.VILLAGER_HAPPY, user.getLocation().add(0, 1, 0), 100, 0.4, 0.5, 0.4, 0.1);
        } else if (a > 0) {
            item.setAmount(item.getAmount() - 1);
            user.setHealth(health + a);
            user.playSound(user, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            user.spawnParticle(Particle.VILLAGER_HAPPY, user.getLocation().add(0, 1, 0), 100, 0.4, 0.5, 0.4, 0.1);
        } else {
            user.sendMessage(Message.msg.deserialize("<gold>[System] 满血不可使用."));
        }
    }

    private static void speed(Player user, ItemStack item) {
        item.setAmount(item.getAmount() - 1);
        Power.addP(PotionEffectType.SPEED, 100, 1, user);
    }

    private static void joker2(PlayerInteractEvent e, ItemStack item, Player user) {
        //Joker-2
        e.setCancelled(true);
        item.setAmount(item.getAmount() - 1);
        user.spawnParticle(Particle.SMOKE_LARGE, user.getLocation(), 100, 0.5, 0.5, 0.5, 0.5);
        user.teleport(Joker.JokerLoc);
        user.playSound(user, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        tasks.add(user.getScheduler().runDelayed(
                plugin,
                t -> user.getInventory().addItem(ItemCreator.create(Material.SLIME_BALL).name(msg.deserialize("<gray>欺诈宝珠[ <red>存</red> ]")).data(2000001).getItem()),
                null, 1200
        ));
    }

    private static void joker1(PlayerInteractEvent e, ItemStack item, Player user) {
        //Joker-1
        e.setCancelled(true);
        user.playSound(user, Sound.UI_BUTTON_CLICK, .5f, 2f);
        item.setAmount(item.getAmount() - 1);
        Joker.JokerLoc = user.getLocation();
        Joker.JokerUser = user.getPlayer();
        user.spawnParticle(Particle.SMOKE_LARGE, user.getLocation(), 100, 0.5, 0.5, 0.5, 0.5);
        tasks.add(user.getScheduler().runDelayed(
                plugin,
                t -> user.getInventory().addItem(ItemCreator.create(Material.SLIME_BALL).name(msg.deserialize("<gray>欺诈宝珠[ <red>返</red> ]")).data(2000002).getItem()),
                null, 20
        ));
    }

    @EventHandler
    void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem().getItemMeta().hasCustomModelData()) {
            var id = e.getItem().getItemMeta().getCustomModelData();
            ItemStack item = e.getItem();
            Player user = e.getPlayer();
            switch (id) {
                case 101 -> compresscookie(e, item, user);
                case 102 -> compresscookie(e, item, user);
            }
        }
    }

    @EventHandler
    void preCraft(PrepareItemCraftEvent e) {
        ItemStack item = e.getInventory().getResult();
        if (item != null) {
            if (tools.contains(item.getType())) {
                e.getInventory().setResult(ItemCreator.create(item.getType()).attribute(Attribute.GENERIC_ATTACK_DAMAGE, 0, EquipmentSlot.HAND).getItem());
            }
        }
    }

    @EventHandler
    void onCraft(CraftItemEvent e) {
        if (Arrays.stream(e.getInventory().getContents()).anyMatch(it -> it.getItemMeta().hasCustomModelData())) {
            e.setCancelled(true);
        }
    }

    private void compresscookie(PlayerItemConsumeEvent e, ItemStack item, Player user) {
        item.setAmount(item.getAmount() - 1);
        user.setFoodLevel(user.getFoodLevel() + 10);
        Power.addP(PotionEffectType.SLOW, 30, 0, user);
    }

    @EventHandler
    void onPlaceBlock(BlockPlaceEvent e) {
        var b = e.getBlockPlaced();
        if (b.getType() != Material.END_PORTAL_FRAME) return;
        var opf = portal_frame.stream().filter(l -> l.getBlockX() == b.getX() && l.getBlockZ() == b.getZ()).findAny();
        if (opf.isEmpty()) {
            e.setCancelled(true);
            return;
        }
        var l = opf.get();
        portal_frame.remove(l);
        Objective frame = h_board.getObjective("frame");
        if (frame != null) {
            frame.getScore("%d %d".formatted(l.getBlockX(), l.getBlockZ())).resetScore();
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.SMOKE_LARGE, l, 200, 1, 1, 1, 0.5, null, true);
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.SMOKE_LARGE, l, 200, 1, 1, 1, 0.5, null, true);
        }
        if (portal_frame.isEmpty()) {
            intoSecond();
        }
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
            case 1000000 -> peter1(e, item, user);
            case 1000001 -> peter2(e, item, user);
            case 1000002 -> kyle(e, item, user);
            case 2000001 -> joker1(e, item, user);
            case 2000002 -> joker2(e, item, user);
            case 3000000 -> heal(user, item);
            case 3000001 -> speed(user, item);
            case 5000000 -> freeze(e, item, user);
        }
    }

    private void freeze(PlayerInteractEvent e, ItemStack item, Player user) {
        e.setCancelled(true);
        //冰冻手雷
        item.setAmount(item.getAmount() - 1);
        user.playSound(user, Sound.BLOCK_GLASS_BREAK, .5f, 2f);
        Vector normalize = user.getLocation().getDirection().normalize();
        if (user.isSneaking()) {
            normalize.multiply(1).setY(0.1);
        } else {
            normalize.multiply(2);
        }
        Location location = user.getLocation().clone().add(0, 2, 0);
        Item item1 = (Item) user.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
        item1.setItemStack(new ItemStack(Material.ICE));
        item(item1, user);
        item1.setVelocity(normalize);
        new BukkitRunnable() {
            Location location1;
            int t = 0;

            @Override
            public void run() {
                t++;
                item1.customName(msg.deserialize("<aqua> %s".formatted(t)));
                item1.setCustomNameVisible(true);
                if (item1.isOnGround()) {
                    location1 = item1.getLocation();
                    item1.remove();
                    user.getWorld().spawnParticle(Particle.REDSTONE, location1, 1000, 4, 1, 4, 10, new Particle.DustOptions(Color.WHITE, .5f), true);
                    location1.getNearbyPlayers(5, player -> !player.equals(user))
                            .forEach(player -> {
                                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                                armor_stand(armorStand);
                                armorStand.setItem(EquipmentSlot.HEAD, ItemCreator.create(Material.ICE).getItem());
                                new BukkitRunnable() {
                                    int t = 0;

                                    @Override
                                    public void run() {
                                        player.getWorld().spawnParticle(Particle.ITEM_CRACK, player.getLocation().clone().add(0, 1, 0), 5, 0.1, 0.1, 0.1, 0.5, new ItemStack(Material.ICE), true);
                                        if (player.isDead()) {
                                            armorStand.remove();
                                            cancel();
                                        }
                                        t++;
                                        player.teleport(armorStand);
                                        if (t % 5 == 0) {
                                            circle(1, player, player.getLocation());
                                            circle(1, player, player.getLocation().clone().add(0, 2, 0));
                                            circle(1, player, player.getLocation().clone().add(0, 1, 0));
                                            user.playSound(user, Sound.BLOCK_GLASS_BREAK, .5f, 1f);
                                        }
                                        if (t >= 30) {
                                            armorStand.remove();
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                                player.damage(5, user);
                            });
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void peter1(PlayerInteractEvent e, ItemStack item, Player user) {
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
        user.playSound(user, Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
        new BukkitRunnable() {
            int t = 0;

            @Override
            public void run() {
                Location location1 = item1.getLocation();
                if (t >= 10) {
                    user.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location1, 10, 1, 1, 1, null);
                    user.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location1, 500, 0.5, 0.5, 0.5, 0.1, null, true);
                    location1.getNearbyPlayers(3, player -> !player.equals(user))
                            .forEach(player -> {
                                player.setVelocity(AtoB(location1, player.getLocation()).setY(0.5));
                                Power.addP(PotionEffectType.SLOW, 30, 1, player);
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

    private void peter2(PlayerInteractEvent e, ItemStack item, Player user) {
        //破片手雷
        e.setCancelled(true);
        item.setAmount(item.getAmount() - 1);
        user.playSound(user, Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
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
                    location1.getNearbyPlayers(3, player -> !player.equals(user))
                            .forEach(player -> player.damage(3, user));
                    for (int count = 0; count <= 10; count++) {
                        popian(location1.clone(), user);
                    }
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

    public void armor_stand(ArmorStand entity) {
        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setVisible(false);
        entity.setMarker(true);
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
            l.getNearbyPlayers(3, player -> !player.equals(p))
                    .forEach(player -> {
                        player.damage(3, player);
                        l.multiply(0);
                    });
        }
    }

}