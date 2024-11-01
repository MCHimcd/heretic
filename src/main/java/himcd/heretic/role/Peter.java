package himcd.heretic.role;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import himcd.heretic.util.RoleEquip;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static himcd.heretic.Heretic.plugin;
import static himcd.heretic.game.GameState.State.NONE;
import static himcd.heretic.game.GameState.state;
import static himcd.heretic.util.Message.msg;

public class Peter extends Role {
    public Peter(Player pl) {
        super(pl);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == NONE) cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void equip() {
        var names = Message.convertMsg(List.of("Peter"));
        RoleEquip.of(p)
                .helmet(ItemCreator.create(Material.SKELETON_SKULL).name(names.pop()).getItem())
                .boots(ItemCreator.create(Material.LEATHER_BOOTS).color(Color.YELLOW).getItem())
                .chestplate(ItemCreator.create(Material.LEATHER_CHESTPLATE).color(Color.YELLOW).getItem())
                .leggings(ItemCreator.create(Material.LEATHER_LEGGINGS).color(Color.YELLOW).getItem())
                .items(
                        ItemCreator.create(Material.SNOWBALL).name(Message.msg.deserialize("<gold><bold>引力手雷</bold> <red><右键>")).lore(Message.msg.deserialize("""
                                     <gray><引力手雷>
                                 <gray>右键可以扔出，蹲下右键可以抛出.
                                 <gray>一段时间后<red><bold>爆炸</bold></red>，吸引周围敌人，造成<red><bold>伤害</bold></red>和<red><bold>减速</bold></red>效果\
                                """)).data(1000000).amount(2).getItem(),

                        ItemCreator.create(Material.CLAY_BALL).name(Message.msg.deserialize("<gold><bold>破片手雷</bold> <red><右键>")).data(1000001).amount(2).getItem(),
                        ItemCreator.create(Material.WOODEN_SWORD).name(msg.deserialize("<gold><bold>彼得<reset>之剑"))
                                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, 4, EquipmentSlot.HAND)
                                .attribute(Attribute.GENERIC_ATTACK_SPEED, 1, EquipmentSlot.HAND)
                                .hideAttributes()
                                .getItem()
                );
    }

    @Override
    public String toString() {
        return "Peter";
    }

}