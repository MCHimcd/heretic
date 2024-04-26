package himcd.heretic.menu;

import himcd.heretic.game.HPlayerInfo;
import himcd.heretic.util.ItemCreator;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static himcd.heretic.TickRunner.prepareTime;
import static himcd.heretic.game.GameState.State;
import static himcd.heretic.game.GameState.state;
import static himcd.heretic.game.HPlayerInfo.player_info;
import static himcd.heretic.util.Message.*;

public class MainMenu extends SlotMenu {
    private static final ItemStack join = ItemCreator.create(Material.STONE).name(msg.deserialize("<reset><green>准备")).getItem();
    private static final ItemStack quit = ItemCreator.create(Material.STONE).name(msg.deserialize("<reset><red>取消准备")).getItem();
    private static final ItemStack docs = ItemCreator.create(Material.BOOK).name(msg.deserialize("<reset><aqua>文档")).getItem();
    public static List<Player> prepared = new ArrayList<>();
    private boolean isPrepared = false;

    public MainMenu(Player player) {
        super(27, msg.deserialize("<gold><b>主菜单"), player);
        //玩家信息
        HPlayerInfo playerInfo = player_info.get(player);
        setSlot(10,
                ItemCreator.create(Material.SKELETON_SKULL)
                        .name(msg.deserialize("<reset><red>玩家信息"))
                        .lore(
                                Component.text("已选角色：%s".formatted(playerInfo.role())),
                                Component.text("已选技能：%s".formatted(playerInfo.skill()))
                        )
                        .getItem(),
                (i, p) -> p.openInventory(new PlayerInfoMenu(player).getInventory()));
        //准备
        var count = Bukkit.getOnlinePlayers().stream().filter(pl -> pl.getGameMode() == GameMode.ADVENTURE).count();
        if (count == 1) {
            setSlot(13, ItemCreator.create(Material.BARRIER).name(msg.deserialize("<dark_red>人数不足")).getItem(), (i, p) -> close = false);
        } else if (state == State.NONE) {
            isPrepared = prepared.contains(player);
            BiConsumer<ItemStack, Player> f = (i, p) -> {
                if (isPrepared) {
                    //已准备
                    prepared.remove(p);
                    getInventory().setItem(1, join);
                    Bukkit.getOnlinePlayers().forEach(pl->{
                        pl.playSound(pl, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                    });
                } else {
                    //未准备
                    prepared.add(p);
                    getInventory().setItem(1, quit);
                    Bukkit.getOnlinePlayers().forEach(pl->{
                        pl.playSound(pl, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,2);
                    });
                }
                if (prepared.size() == 1)
                    prepareTime = -1;
                else if (prepared.size() == count)
                    prepareTime = 20;
                else prepareTime = 1200;
                isPrepared = !isPrepared;
                close = false;
            };
            setSlot(13, isPrepared ? quit : join, f);
        } else
            setSlot(13, ItemCreator.create(Material.BARRIER).name(msg.deserialize("<dark_red>游戏已开始")).getItem(), (i, p) -> close = false);
        setSlot(16, docs, (i, p) -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.addScoreboardTag("docs");
            p.openBook(Book.builder().pages(convertMsg(List.of(
                    "test",
                    "test2"
            ))).build());
            p.teleport(p);
        });
    }
}