package himcd.heretic.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MainMenu extends SlotMenu {
    public MainMenu() {
        super(27, Component.text("主菜单", NamedTextColor.GOLD, TextDecoration.BOLD));
        //TODO
    }
}