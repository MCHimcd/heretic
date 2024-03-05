package himcd.heretic.role;

import himcd.heretic.util.EquipmentFactory;
import himcd.heretic.util.Message;
import himcd.heretic.util.RoleEquip;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class Peter extends Role{
    public Peter(Player pl) {
        super(pl);
    }
    @Override
    public void equip() {
        var names= Message.convertMsg(List.of("test"));
        RoleEquip.of(p)
                .helmet(EquipmentFactory.create(Material.SKELETON_SKULL)
                        .name(names.pop())
                        .getItem());
    }

    @Override
    public String toString() {
        return "Peter";
    }
}