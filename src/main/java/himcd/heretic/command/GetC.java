package himcd.heretic.command;

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
        int S1 = Integer.parseInt(args[1]);
        if (S1 < 1) return false;
        if (S.equals("1")) {
            for (int c = S1; c >= 1; c--) {
                p.getInventory().addItem(Role.getSkillItem(1));
            }
        }
        /// TODO: 3/23/2024   制作Factory直接对应数字
        return true;
    }
}