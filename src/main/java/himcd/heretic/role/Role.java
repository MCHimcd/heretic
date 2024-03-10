package himcd.heretic.role;

import org.bukkit.entity.Player;

public abstract class Role {
    protected final Player p;

    public Role(Player pl) {
        p = pl;
    }

    public static Role of(Player pl, String name) {
        return switch (name) {
            case "Peter" -> new Peter(pl);
            default -> null;
        };
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