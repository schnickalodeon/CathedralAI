package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Turnable;

import java.awt.*;

public class Inn extends Building {
    public Inn(Player player) {
        super("Inn",
                Turnable.Full, player, new Point(0,0),new Point(1,0), new Point(1,1));
    }
}
