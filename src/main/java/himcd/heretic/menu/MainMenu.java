package himcd.heretic.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static himcd.heretic.Heretic.msg;

public class MainMenu extends SlotMenu {
    public MainMenu() {
        super(27, msg.deserialize("<gold><b>主菜单"));
        setSlot(0,new ItemStack(Material.STONE){{
            editMeta(meta->meta.displayName(msg.deserialize("<gold>test")));
        }},i->{});
    }
}