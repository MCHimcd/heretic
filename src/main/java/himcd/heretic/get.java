package himcd.heretic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class get implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            return false;
        }
        String S = args[0];
        int S1 = Integer.parseInt(args[1]);
        if (S.equals("1")){
            for (int c=S1;c>=1;c--){
                p.getInventory().addItem(ItemFactory.S1);
            }
        }
        return true;
    }
}
