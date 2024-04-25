package himcd.heretic.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static himcd.heretic.Heretic.believerT;
import static himcd.heretic.Heretic.msb;
import static himcd.heretic.game.GameListener.items;
import static himcd.heretic.game.GameState.*;
import static himcd.heretic.game.HPlayer.heretic;
import static himcd.heretic.game.HPlayer.players;
import static himcd.heretic.util.Message.msg;

public final class GameRunner extends BukkitRunnable {
    public static Location location =null;
    int supplyx;
    int supplyz;
    World world = Bukkit.getWorld("world");
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
            case SECOND -> heretic.power().getBuff2().accept(heretic);
            case ENDING -> players.keySet().forEach(p -> p.damage(0.1));
        }
        //补给道具
        if (location==null){
            if (supplyTime<=3600){
                supplyTime++;
        }else supplyTime=0;
        if (supplyTime == 3600){
            supplyTime=3601;
            var r =new Random();
            var x = r.nextInt(-128,128);
            var z = r.nextInt(-128,128);
            location = new Location(world,x,100,z);
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                player.sendMessage(msg.deserialize("<gold>[System] 空投发放。坐标为:<red><bold> %s , %s".formatted(location.getX(),location.getZ())));
            });
        }
    }else {
            if (location.getBlock().getType().isAir()){
                location.add(0,-0.3,0);
                world.spawnParticle(Particle.REDSTONE,location,20,0.1,0.1,0.1,0.1,new Particle.DustOptions(Color.WHITE,1f),true);
            }else {
                var r =new Random();
                world.spawnParticle(Particle.FIREWORKS_SPARK,location,50,0.4,0.4,0.4,0.5,null,true);
                location.getNearbyPlayers(2, Player::isSneaking)
                        .forEach(player -> {
                            int i = r.nextInt(0, items.size());
                            String ia = "%s".formatted(i);
                            player.getInventory().addItem(items.get(ia));
                            Bukkit.getServer().getOnlinePlayers().forEach(player1 -> {
                                player1.sendMessage(msg.deserialize("<gold>[System] 空投已被玩家:%s拾取".formatted(player.getName())));
                            });
                            location=null;
                        });
            }
        }
}}