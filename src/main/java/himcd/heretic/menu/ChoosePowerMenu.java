package himcd.heretic.menu;

import net.kyori.adventure.text.Component;

import static himcd.heretic.Heretic.msg;

public class ChoosePowerMenu extends SlotMenu{
    public ChoosePowerMenu() {
        super(9, msg.deserialize("<gold>选择你的"));

    }
}