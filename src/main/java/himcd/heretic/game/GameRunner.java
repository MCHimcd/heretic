package himcd.heretic.game;

import himcd.heretic.role.power.Power;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Random;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.game.GameListener.items;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.heretic;
import static himcd.heretic.game.HPlayer.players;
import static himcd.heretic.util.Message.msg;
import static himcd.heretic.util.Message.rMsg;

public final class GameRunner extends BukkitRunnable {
    public static Location location = null;
    public static ArmorStand armorStand = null;
    public static BlockDisplay blockDisplay = null;
    World world = Bukkit.getWorld("world");

    public void armor_stand(ArmorStand entity) {
        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setVisible(false);
        entity.setMarker(true);
        entity.setHeadPose(EulerAngle.ZERO.setY(-90));
    }

    @Override
    public void run() {
        gameTime++;
        if (gameTime == 24000) intoSecond();
        else if (gameTime == 36000) intoEnding();
        switch (state) {
            // 游戏逻辑
            case FIRST -> {
                var h = heretic.player();
                var ps = h.getWorld().getNearbyPlayers(h.getLocation(), 10)
                        .stream().filter(p -> believerT.hasEntity(p)).toList();
                heretic.power().getBuff1().accept(heretic, ps);
            }
            case SECOND -> {
                heretic.power().getBuff2().accept(heretic);
                Bukkit.getOnlinePlayers().forEach(p -> Power.addP(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, p));
            }
            case ENDING -> players.keySet().forEach(p -> p.damage(0.1));
        }
        if (heretic != null) {
            var h = heretic.player();
            if (!h.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                Power.addP(PotionEffectType.HEALTH_BOOST, 1000000, 4, h);
            }
        }
        //补给道具
        if (location == null) {
            if (supplyTime <= 3600) {
                supplyTime++;
            } else supplyTime = 0;
            if (supplyTime == 3600) {
                supplyTime = 3601;
                var r = new Random();
                var x = r.nextInt(-128, 128);
                var z = r.nextInt(-128, 128);
                location = new Location(world, x, 100, z);
                blockDisplay = (BlockDisplay) world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
                blockDisplay.setBlock(Material.CHEST.createBlockData());
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(rMsg("<gold>[System] 空投发放.坐标为:<red><bold> x=%s , z=%s".formatted(location.getX(), location.getZ()))));
            }
        } else {
            if (location.getBlock().getType().isAir()) {
                location.add(0, -0.2, 0);
                blockDisplay.teleport(location.clone().add(0, 0.3, 0));
                world.spawnParticle(Particle.REDSTONE, location.clone().add(0.5, 0, 0.5), 20, 0.1, 0.1, 0.1, 0.1, new Particle.DustOptions(Color.WHITE, 1f), true);
                world.spawnParticle(Particle.END_ROD, location.clone().add(0.5, 0, 0.5), 0, 1, 0, 0, 1, null, true);
                world.spawnParticle(Particle.END_ROD, location.clone().add(0.5, 0, 0.5), 0, -1, 0, 0, 1, null, true);
                world.spawnParticle(Particle.END_ROD, location.clone().add(0.5, 0, 0.5), 0, 0, 0, 1, 1, null, true);
                world.spawnParticle(Particle.END_ROD, location.clone().add(0.5, 0, 0.5), 0, 0, 0, -1, 1, null, true);
            } else {
                var r = new Random();
                world.spawnParticle(Particle.FIREWORKS_SPARK, location.clone().add(0.5, 0, 0.5), 10, 0.1, 0.2, 0.1, 0.05, null, true);
                world.spawnParticle(Particle.TOTEM, location.clone().add(0.5, 0, 0.5), 10, 0.1, 0.2, 0.1, 0.5, null, true);
                location.clone().add(0.5, 0, 0.5).getNearbyPlayers(2, player -> player.isSneaking() && player.getGameMode() != GameMode.SPECTATOR)//todo 持续1s
                        .forEach(player -> {
                            int i = r.nextInt(0, items.size());
                            String iS = "%s".formatted(i);
                            player.getInventory().addItem(items.get(iS));
                            Bukkit.getServer().getOnlinePlayers().forEach(player1 -> player1.sendMessage(msg.deserialize("<gold>[System] 空投已被玩家:%s拾取".formatted(player.getName()))));
                            if (blockDisplay != null) blockDisplay.remove();
                            location = null;
                        });
            }
        }
    }
}