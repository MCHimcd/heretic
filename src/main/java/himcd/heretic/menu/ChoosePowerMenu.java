package himcd.heretic.menu;

import org.bukkit.entity.Player;

import static himcd.heretic.util.Message.msg;

public class ChoosePowerMenu extends SlotMenu{
    public ChoosePowerMenu(Player player) {
        super(9, msg.deserialize("<gold>选择你的力量"),player);

    }
}