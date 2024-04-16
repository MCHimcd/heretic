package himcd.heretic.role.skill;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static himcd.heretic.util.Message.msg;

public class Heal extends Skill {
    public Heal(Player pl) {
        super(pl);
    }

    @Override
    public void giveItem() {
        player.getInventory().addItem(
                ItemCreator.create(Material.GOLD_INGOT).name(msg.deserialize("<gold>治疗")).data(3000000).getItem()
        );
    }

}