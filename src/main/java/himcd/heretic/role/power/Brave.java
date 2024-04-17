package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Brave extends Power{

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return (h,ps)->{
            if (ps.size()>=2){
                h.player().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,3,0,true,false));
            }
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3,0,true,false));
        };
    }
    @Override
    public Consumer<HPlayer> getBuff2() {
        return h->{
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3,1,true,false));
            h.player().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,3,1,true,false));
        };
    }
}