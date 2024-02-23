package himcd.heretic.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static himcd.heretic.Heretic.msg;

public class MainMenu extends SlotMenu {
    public MainMenu() {
        super(27, msg.deserialize("<gold><b>主菜单"));
        setSlot(0, new ItemStack(Material.SKELETON_SKULL) {{
            editMeta(meta -> meta.displayName(msg.deserialize("<red>个人信息")));
        }}, (i, p) -> {
            p.sendMessage(msg.deserialize(
                    "<rainbow>" + p.getName()
                            + "\n<gray><bold>-----------------</bold>\n<gold>异教徒获胜场次 %s".formatted(Hwins.getScoreFor(p).getScore())
                            + "\n<gold>信徒徒获胜场次 %s\n<gray><bold>-----------------".formatted(Bwins.getScoreFor(p).getScore())
            ));
        });
    }
}