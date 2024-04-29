package himcd.heretic.role.power;

import himcd.heretic.game.HPlayer;
import org.bukkit.Material;
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
        return new ItemStack(Material.AIR);
    }

    @Override
    public BiConsumer<HPlayer, List<Player>> getBuff1() {
        return (h,ps)->{
            addP(PotionEffectType.INCREASE_DAMAGE,3,0,h.player());
        };
    }
    @Override
    public Consumer<HPlayer> getBuff2() {
        return h->{
            addP(PotionEffectType.INCREASE_DAMAGE,3,1,h.player());
            addP(PotionEffectType.SPEED,3,0,h.player());
        };
    }
}