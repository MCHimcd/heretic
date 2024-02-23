package himcd.heretic;

import himcd.heretic.game.GameState;
import himcd.heretic.menu.MainMenu;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class Heretic extends JavaPlugin implements Listener {
    public static final MiniMessage msg = MiniMessage.miniMessage();
    public static Heretic plugin;
    public static BukkitTask tick_task;
    public static Scoreboard msb;
    public static Team hereticT, believerT;
    private final TickRunner tick = new TickRunner();

    @Override
    public void onEnable() {
        plugin = this;
        tick_task = tick.runTaskTimer(this, 0, 1);
        msb = Bukkit.getScoreboardManager().getMainScoreboard();
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
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        GameState.reset();
        getLogger().info("test");
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

    @EventHandler
    void onUse(PlayerInteractEvent e){
        var item=e.getItem();
        if (item == null||item.getType()!= Material.CLOCK) return;
        var p=e.getPlayer();
        p.openInventory(new MainMenu().getInventory());
    }

    @EventHandler
    void onClick(InventoryClickEvent e){
        if(!(e.getInventory().getHolder() instanceof MainMenu m)) return;
        m.handleClick(e.getSlot());
    }
}