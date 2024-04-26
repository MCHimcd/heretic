package himcd.heretic.command;

import himcd.heretic.random;
import himcd.heretic.role.Peter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import static himcd.heretic.game.GameRunner.location;
import static himcd.heretic.game.GameState.*;

import static himcd.heretic.Heretic.plugin;

public class GetC implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            return false;
        }
        String S = args[0];
        if (S.equals("1")) {
            supplyTime=3595;
        }
        if (S.equals("2")) {
            p.teleport(location);
//            random.generateTerrain(p.getWorld(),new Location(p.getWorld(),0,0,0));
//            CustomTerrainGenerator.generateTerrain(p.getWorld(), 0, 0);
        }
        if (S.equals("reset")){
            random.generateTerrain(p.getWorld(),new Location(p.getWorld(),0,0,0));
        }

        return true;
    }
    public void armor_stand(ArmorStand entity) {
        entity.setCustomNameVisible(false);
        entity.setSilent(true);
        entity.setMarker(true);
        entity.setArms(true);
        entity.setHeadPose(EulerAngle.ZERO.setY(-90));
    }
//        if (S.equals("2")){
//            for (int c=S1;c>=1;c--){
//                p.getInventory().addItem(Role.getSkillItem(2000000));
//            }
//        }
}