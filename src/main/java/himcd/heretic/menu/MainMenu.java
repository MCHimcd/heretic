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
                    "       <gray><bold><click:change_page:1>[异端]</click></gray> \n<click:change_page:2>背景介绍</click>\n<click:change_page:3>规则介绍</click>\n<click:change_page:10>角色介绍</click>",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray> 背景介绍\n在一个古老的帝国中，四大宗教分别掌控着四方的信仰与权力:东方的<gold><bold><click:change_page:6>光明教派</click></gold>，南方的<aqua><bold><click:change_page:7>天命宗</click></aqua>，西方的<blue><bold><click:change_page:8>星海真灵</click></blue>，北方的<light_purple><bold><click:change_page:9>静思道场</click></light_purple>。这四大宗教各自维护着自己的教义和统治，彼此之间既有合作也有竞争，维持着一种微妙的平衡。",
                    "在帝国的中心，首都皇城附近，有一个小村庄，村中的青年名叫<red><bold>云逸</red>。他自小接触到四大宗教，但他对每个宗教中的某些观点都持有异议。云逸的想法让他在各个教派之间都被视为异端。他的观点虽然受到一些人的暗中赞同，但更多的是被主流信徒所排斥。云逸暗暗创建一个宗教，名唤<black><bold>魂教</black>,教徒虽不多，但各个身手不得了。",
                    "于是长达百年的争端就此开始，魂教众人将各教的重要文件，通过特定的方式传达至主教。而各教之间虽一致抵抗此举，但相互之间也各有争斗。",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray> 规则介绍\n游戏开始时分异教徒和教徒，异教徒的任务是在规定位置放置末地传送门，将坐标上传，当全部坐标上传后，杀掉阻止你的任何人,存活下来。 ",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray>\n<gold><bold><click:change_page:6>光明教派</click></gold>\n光明教派是一个强调光明、智慧与启示的宗教。它代表了追求精神觉醒与心灵和平的信仰。\n信徒普遍力大，灵活。",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray>\n<aqua><bold><click:change_page:7>天命宗</click></aqua>\n天命宗强调天意和预言的宗教，突出预言和神的旨意对信徒生活的指导作用。它可能包含许多关于未来预言和神圣启示的教义。\n教内机械发达。信徒配备一些科技物品。",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray>\n<blue><bold><click:change_page:8>星海真灵</click></blue>\n星海真灵是一个与宇宙能量和自然法则紧密相关的宗教。这种宗教可能强调宇宙中的生命力量，以及通过宇宙的理解来寻求人生的真理和指南。\n信徒一般有着可以和自然沟通的能力。",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray>\n<light_purple><bold><click:change_page:9>静思道场</click></light_purple>\n静思道场传达了一种内在平静和自我反思的宗教氛围，适合那些强调静坐冥想、心灵净化及与自然和谐相处的宗教。信徒可能通过静坐和冥想来达到精神上的高度清洁和自我提升。\n信徒一般拥有着特殊的能力。",
                    "       <gray><bold><click:change_page:1>[异端]</click></gray>\n<click:change_page:11>皮特</click>\n<click:change_page:12>克洛伊</click>\n<click:change_page:13>凯尔</click>",
                    "皮特\n背景故事:\n皮特原隶属于天命宗，因在宗门受到欺压，下定决心逃离宗门，在逃离过程中遇到云逸，成为魂宗创始人之一\n技能:拥有引力手雷和破片手雷各两颗。",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""

            ))).build());
            p.teleport(p);
        });
    }
}