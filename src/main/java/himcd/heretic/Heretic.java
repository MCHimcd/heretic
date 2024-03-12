package himcd.heretic;

import himcd.heretic.game.GameState;
import himcd.heretic.menu.ChoosePowerMenu;
import himcd.heretic.menu.MainMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static himcd.heretic.game.GameState.State;
import static himcd.heretic.game.GameState.state;

@SuppressWarnings("SpellCheckingInspection")
public final class Heretic extends JavaPlugin implements Listener {

    public static Heretic plugin;
    public static BukkitTask tick_task;
    public static Scoreboard msb;
    public static Team hereticT, believerT;
    public static Objective Hwins, Bwins;
    private final TickRunner tick = new TickRunner();

    @Override
    public void onEnable() {
        //初始化
        plugin = this;
        tick_task = tick.runTaskTimer(this, 0, 1);
        msb = Bukkit.getScoreboardManager().getMainScoreboard();
        //队伍
        hereticT = msb.getTeam("heretic");
        if (hereticT == null) {
            hereticT = msb.registerNewTeam("heretic");
            hereticT.color(NamedTextColor.DARK_RED);
            hereticT.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            hereticT.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        }
        believerT = msb.getTeam("believer");
        if (believerT == null) {
            believerT = msb.registerNewTeam("believer");
            believerT.color(NamedTextColor.DARK_GREEN);
            believerT.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        }
        //计分板
        Hwins = msb.getObjective("Hwins"); //异教徒获胜场次
        if (Hwins == null) Hwins = msb.registerNewObjective("Hwins", Criteria.DUMMY, Component.empty());
        Bwins = msb.getObjective("Bwins"); //信徒获胜场次
        if (Bwins == null) Bwins = msb.registerNewObjective("Bwins", Criteria.DUMMY, Component.empty());
        //事件
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        //重置
        GameState.reset();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();
        p.getInventory().clear();
        p.clearActivePotionEffects();
        p.setGameMode(GameMode.ADVENTURE);
        Team team = msb.getEntityTeam(p);
        if (team != null) {
            team.removeEntity(p);
        }
    }

    //主菜单
    @EventHandler
    void onUse(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND
                || e.getAction() == Action.LEFT_CLICK_AIR
                || e.getAction() == Action.LEFT_CLICK_BLOCK)
            return;
        var item = e.getItem();
        if (item == null || item.getType() != Material.CLOCK) return;
        var p = e.getPlayer();
        p.openInventory(new MainMenu(p).getInventory());
    }

    @EventHandler
    void onClick(InventoryClickEvent e) {
        if (state == State.NONE || state == State.PREPARE) e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player p) || !(e.getInventory().getHolder() instanceof MainMenu m)) return;
        m.handleClick(e.getSlot());
    }

    @EventHandler
    void onClose(InventoryCloseEvent e) {
        if (state != State.PREPARE) return;
        //防止关闭选择菜单
        if (e.getInventory().getHolder() instanceof ChoosePowerMenu m) {
            e.getPlayer().openInventory(m.getInventory());
        }
    }
}