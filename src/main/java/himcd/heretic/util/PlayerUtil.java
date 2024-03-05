package himcd.heretic.util;

import himcd.heretic.game.GameState;
import himcd.heretic.game.HPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    public static List<HPlayer> getPlayers(){
        var l= new ArrayList<>(GameState.believers);
        l.add(GameState.heretic);
        return l;
    }


}