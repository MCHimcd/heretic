package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.RoleEquip;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameListener.kylelocation;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;
import static himcd.heretic.util.Message.msg;

public class Kyle extends Role{
    public Kyle(Player pl) {
        super(pl);
        new BukkitRunnable(){
           int kyleTime=0;
            @Override
            public void run() {
                if (state==NONE)cancel();
                if (kylelocation!=null){
                    if (!kylelocation.getNearbyPlayers(10,player -> player!=pl).isEmpty()){
                        kyleTime++;
                        if (kyleTime>=100){
                            kylelocation.getNearbyPlayers(10,player -> player!=pl).forEach(player -> {

                            });
                        }
                    }else kyleTime=0;
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    @Override
    public void equip() {
        RoleEquip.of(p)
                .boots(ItemCreator.create(Material.LEATHER_BOOTS).color(Color.WHITE).getItem())
                .chestplate(ItemCreator.create(Material.LEATHER_CHESTPLATE).color(Color.WHITE).getItem())
                .leggings(ItemCreator.create(Material.LEATHER_LEGGINGS).color(Color.WHITE).getItem())
                .items(ItemCreator.create(Material.WOODEN_SWORD).name(msg.deserialize("<gold><bold>凯尔<reset>"))
                                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,7, EquipmentSlot.HAND)
                                .attribute(Attribute.GENERIC_ATTACK_SPEED,-1.2,EquipmentSlot.HAND)
                                .hideAttributes()
                                .getItem());

    }
}
