package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import himcd.heretic.util.RoleEquip;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;

public class Peter extends Role {
    public Peter(Player pl) {
        super(pl);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (state==NONE)cancel();
                pl.sendMessage("1");
            }
        }.runTaskTimer(plugin,0,1);
    }

    @Override
    public void equip() {
        var names = Message.convertMsg(List.of("Peter"));
        RoleEquip.of(p)
                .helmet(ItemCreator.create(Material.SKELETON_SKULL).name(names.pop()).getItem())
                .items(
                        ItemCreator.create(Material.SNOWBALL).name(Message.msg.deserialize("<gold><bold>引力手雷</bold> <red><右键>")).lore(Message.msg.deserialize("""
                                 <gray><引力手雷>
                             <gray>右键可以扔出，蹲下右键可以抛出.
                             <gray>一段时间后<red><bold>爆炸</bold></red>，吸引周围敌人，造成<red><bold>伤害</bold></red>和<red><bold>减速</bold></red>效果\
                            """)).data(1000000).getItem(),
                        ItemCreator.create(Material.CLAY_BALL).name(Message.msg.deserialize("<gold><bold>破片手雷</bold> <red><右键>")).data(1000001).getItem()
                );
    }

    @Override
    public String toString() {
        return "Peter";
    }

}