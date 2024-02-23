package himcd.heretic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class GameListener implements Listener {
    @EventHandler
    void onUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR
                || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        var item = e.getItem();
        if (item == null || !item.getItemMeta().hasCustomModelData()) return;
        var id = item.getItemMeta().getCustomModelData();
        switch (id) {
            case 10000 -> {
                e.getPlayer().sendMessage("你使用了技能");
            }
        }
    }
}