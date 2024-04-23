package himcd.heretic.menu;

import himcd.heretic.game.HPlayerInfo;
import himcd.heretic.role.Role;
import himcd.heretic.role.skill.Skill;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

import static himcd.heretic.game.HPlayerInfo.player_info;
import static himcd.heretic.util.Message.rMsg;

public class ChooseRSMenu extends SlotMenu {
    public ChooseRSMenu(Player p, boolean role) {
        super(27, rMsg(role ? "<light_purple>选择角色" : "<yellow>选择通用技能"), p);
        AtomicInteger i = new AtomicInteger();
        if (role) {
            Role.chooseItems.forEach((n, item) -> {
                setSlot(i.get(), item, (it, pl) ->
                        player_info.put(pl, new HPlayerInfo(n, player_info.get(pl).skill())));
                i.getAndIncrement();
            });
        } else Skill.chooseItems.forEach((n, item) -> {
            setSlot(i.get(), item, (it, pl) ->
                    player_info.put(pl, new HPlayerInfo(player_info.get(pl).role(), n)));
            i.getAndIncrement();
        });
    }
}