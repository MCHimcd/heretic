package himcd.heretic.command;

import himcd.heretic.CustomTerrainGenerator;
import himcd.heretic.role.Peter;
import himcd.heretic.role.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        if (S.equals("2")){
            CustomTerrainGenerator.generateTerrain(p.getWorld(),0,0);
        }
//        if (S.equals("2")){
//            for (int c=S1;c>=1;c--){
//                p.getInventory().addItem(Role.getSkillItem(2000000));
//            }
//        }
        return true;
    }
}