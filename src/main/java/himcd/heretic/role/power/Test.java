package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Test extends Power{
    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return (h,ps)->{
            //todo
            if (ps.size()>=2){
                h.player().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,3,1,true));
            }
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3,1,true));
        };
    }

    @Override
    public Consumer<HPlayer> getBuff2() {
        return h->{
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3,2,true));
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,3,2,true));
            //todo
        };
    }
}