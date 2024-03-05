package himcd.heretic.role;

import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class Role {
    protected final Player p;
    public Role(Player pl) {
        p = pl;
    }

    //物品、装备
    abstract public void equip();

    @Override
    public String toString() {
        return "";
    }

    public Player getPlayer() {
        return p;
    }

}