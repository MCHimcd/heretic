package himcd.heretic.role;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;

public class Chloe extends Role {
    public Chloe(Player pl) {
        super(pl);
        //二段跳
        new BukkitRunnable(){
            int jumpcount=0;
            int jumpcd=0;
            @Override
            public void run() {
                if (state==NONE)cancel();
                if (!pl.isOnGround()){
                    if (pl.isSneaking()){
                        if (jumpcd==0){
                            if (jumpcount>0){
                                pl.setVelocity(pl.getLocation().getDirection().normalize().multiply(1.3).setY(0.5));
                                pl.getWorld().spawnParticle(Particle.CLOUD,pl.getLocation(),30,0.3,0,0.3,0);
                                jumpcount--;
                                jumpcd=10;
                                pl.setFallDistance(0);
                            }
                        }
                    }
                }else {
                    if (jumpcount<=1){
                        if (pl.getFoodLevel()>10){
                            pl.setFoodLevel(pl.getFoodLevel()-2);
                            jumpcount++;
                        }
                    }
                    jumpcd=8;
                }
                if (jumpcd>0)jumpcd--;
            }
        }.runTaskTimer(plugin,0,1);
    }

    @Override
    public void equip() {

    }
}
