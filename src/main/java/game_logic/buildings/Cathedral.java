package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Cathedral extends Building {
    public Cathedral() {
        super("Cathedral", Turnable.Full, PlayerColor.NEUTRAL,
                new Point(-1,0),
                new Point(0,-1),
                new Point(0,0),
                new Point(0,1),
                new Point(0,2),
                new Point(1,0));
    }
}
