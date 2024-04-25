package himcd.heretic.role.skill;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static himcd.heretic.util.Message.rMsg;

public class Speed extends Skill{
    public Speed(Player pl) {
        super(pl);
    }

    @Override
    public void giveItem() {
        player.getInventory().addItem(
                ItemCreator.create(Material.IRON_INGOT).name(rMsg("<gold>速度")).data(3000001).getItem()
        );

    }
}
