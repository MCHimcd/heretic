package himcd.heretic.command;

import himcd.heretic.CustomTerrainGenerator;
import himcd.heretic.role.Peter;
import himcd.heretic.role.Role;
import io.papermc.paper.math.Rotations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

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
            new Peter(p).equip();
        }
        if (S.equals("2")) {
            CustomTerrainGenerator.generateTerrain(p.getWorld(), 0, 0);
        }
        if (S.equals("3")){
            ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            armor_stand(armorStand);
            new BukkitRunnable(){
                int t =0;

                @Override
                public void run() {
                    t++;
                    Location l1 = p.getLocation();
                    float Yaw = p.getYaw();
                    Location l2 = armorStand.getLocation();
                    armorStand.setHeadRotations(armorStand.getHeadRotations().add(1,0,0));
                    if (t>=200){
                        armorStand.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(plugin,0,1);
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