package himcd.heretic.menu;

import himcd.heretic.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static himcd.heretic.Heretic.Bwins;
import static himcd.heretic.Heretic.Hwins;
import static himcd.heretic.game.HPlayerInfo.player_info;
import static himcd.heretic.util.Message.msg;
import static himcd.heretic.util.Message.rMsg;

public class PlayerInfoMenu extends SlotMenu {

    public PlayerInfoMenu(Player pl) {
        super(9, rMsg("<red>玩家信息"), pl);
        setSlot(0, ItemCreator.create(Material.SKELETON_SKULL).name(rMsg("<reset><aqua>查看胜场数")).getItem(), (i, p) -> p.sendMessage(msg.deserialize(
                "<rainbow>" + p.getName()
                        + "\n<gray><bold>-----------------</bold>\n<gold>异教徒获胜场次 %s".formatted(Hwins.getScoreFor(p).getScore())
                        + "\n<gold>信徒徒获胜场次 %s\n<gray><bold>-----------------".formatted(Bwins.getScoreFor(p).getScore())
        )));
        setSlot(1, ItemCreator.create(Material.ARROW).name(rMsg("<light_purple>选择角色")).lore(rMsg("当前角色：%s".formatted(player_info.get(pl).role()))).getItem(),
                (i, p) -> p.openInventory(new ChooseRSMenu(pl, true).getInventory()));
        setSlot(2, ItemCreator.create(Material.CARROT).name(rMsg("<yellow>选择通用技能")).lore(rMsg("当前角色：%s".formatted(player_info.get(pl).skill()))).getItem(),
                (i, p) -> p.openInventory(new ChooseRSMenu(pl, false).getInventory()));
    }
}