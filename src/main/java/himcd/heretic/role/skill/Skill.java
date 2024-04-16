package himcd.heretic.role.skill;

import org.bukkit.entity.Player;

import static himcd.heretic.util.Message.msg;

public abstract class Skill {
    protected Player player;

    public Skill(Player pl) {
        player = pl;
    }

    public static Skill of(Player pl, String name) {
        return switch (name) {
            default -> new Heal(pl);
        };
    }

    public abstract void giveItem();

}