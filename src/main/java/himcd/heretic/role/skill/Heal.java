package himcd.heretic.role.skill;

import himcd.heretic.util.Message;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class Heal extends Skill {
    public Heal(Player pl) {
        super(pl);
    }

    @Override
    public void use() {
        double health = player.getHealth();
        //noinspection DataFlowIssue
        double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double a = max - health;
        if (a >= 8) {
            player.setHealth(health + 8);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0, 1);
            player.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 100, 0.4, 0.5, 0.4, 0.1);
        } else if (a > 0) {
            player.setHealth(health + a);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0, 1);
            player.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 100, 0.4, 0.5, 0.4, 0.1);
        } else {
            player.sendMessage(Message.msg.deserialize("<gold>[System] 满血不可使用."));
        }

    }
}