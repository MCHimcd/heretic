package himcd.heretic.menu;

import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static himcd.heretic.Heretic.Bwins;
import static himcd.heretic.Heretic.Hwins;
import static himcd.heretic.util.Message.msg;

public class PlayerInfoMenu extends SlotMenu {

    public PlayerInfoMenu(Player pl) {
        super(9, msg.deserialize("<reset><red>玩家信息"), pl);
        setSlot(0, ItemCreator.create(Material.SKELETON_SKULL).name(Message.msg.deserialize("<reset><aqua>查看胜场数")).getItem(), (i, p) -> p.sendMessage(Message.msg.deserialize(
                "<rainbow>" + p.getName()
                        + "\n<gray><bold>-----------------</bold>\n<gold>异教徒获胜场次 %s".formatted(Hwins.getScoreFor(p).getScore())
                        + "\n<gold>信徒徒获胜场次 %s\n<gray><bold>-----------------".formatted(Bwins.getScoreFor(p).getScore())
        )));
        //todo 选角色、技能
    }
}