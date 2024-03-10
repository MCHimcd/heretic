package himcd.heretic.menu;

import himcd.heretic.TickRunner;
import himcd.heretic.util.ItemCreator;
import himcd.heretic.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static himcd.heretic.Heretic.Bwins;
import static himcd.heretic.Heretic.Hwins;

public class MainMenu extends SlotMenu {
    private static final ItemStack info = ItemCreator.create(Material.SKELETON_SKULL).name(Message.msg.deserialize("<red>个人信息")).getItem();
    private static final ItemStack join = ItemCreator.create(Material.STONE).name(Message.msg.deserialize("<green>准备")).getItem();
    private static final ItemStack quit = ItemCreator.create(Material.STONE).name(Message.msg.deserialize("<red>取消准备")).getItem();
    private static final ItemStack docs= ItemCreator.create(Material.BOOK).name(Message.msg.deserialize("<aqua>文档")).getItem();
    public static List<Player> prepared = new ArrayList<>();
    private boolean isPrepared = false;

    public MainMenu(Player player) {
        super(27, Message.msg.deserialize("<gold><b>主菜单"), player);
        //个人信息
        setSlot(0, info
                , (i, p) -> {
                    p.sendMessage(Message.msg.deserialize(
                            "<rainbow>" + p.getName()
                                    + "\n<gray><bold>-----------------</bold>\n<gold>异教徒获胜场次 %s".formatted(Hwins.getScoreFor(p).getScore())
                                    + "\n<gold>信徒徒获胜场次 %s\n<gray><bold>-----------------".formatted(Bwins.getScoreFor(p).getScore())
                    ));
                });
        //准备
        isPrepared = prepared.contains(player);
        BiConsumer<ItemStack, Player> f = (i, p) -> {
            if (isPrepared) {
                //已准备
                prepared.remove(p);
                getInventory().setItem(1, join);
            } else {
                //未准备
                prepared.add(p);
                getInventory().setItem(1, quit);
                if(prepared.size()== Bukkit.getOnlinePlayers().size()) TickRunner.prepareTime=200;
                else TickRunner.prepareTime=1200;
            }
            isPrepared = !isPrepared;
        };
        setSlot(1, isPrepared ? quit : join, f);
        //todo 文档
    }
}