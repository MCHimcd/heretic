package himcd.heretic;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ItemFactory {
    public static final ItemStack S1 = ItemCreator.create(Material.SNOWBALL).name(Message.msg.deserialize("<gold><bold>引力手雷</bold> <red><右键>")).lore(Message.msg.deserialize( "<gray><引力手雷>"+"<gray>右键可以扔出，蹲下右键可以抛出."+"<gray>一段时间后<red><bold>爆炸</bold></red>，吸引周围敌人，造成<red><bold>伤害</bold></red>和<red><bold>减速</bold></red>效果")).data(1000000).getItem();
    public static final ItemStack S2 = ItemCreator.create(Material.CLAY).name(Message.msg.deserialize("<gold><bold>破片手雷</bold> <red><右键>")).data(2000000).getItem();

}
