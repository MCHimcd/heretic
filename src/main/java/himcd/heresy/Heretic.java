package himcd.heresy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Heretic extends JavaPlugin {

    @Override
    public void onEnable() {
        var g=new PerlinNoiseGenerator(new Random());
        for (int i = 0; i < 10; i++) {
            var l=new ArrayList<Integer>();
            for (int j = 0; j < 10; j++) {
                l.add((int)(g.noise(i,j)*10));
            }
            getLogger().info(l.toString());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}