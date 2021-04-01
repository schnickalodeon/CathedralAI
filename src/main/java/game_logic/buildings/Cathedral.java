package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Shape;
import game_logic.Turnable;

import java.awt.*;

public class Cathedral extends Building {
    public Cathedral() {
        super("Cathedral", new Shape(
                new Point(-1,0),
                new Point(0,-1),
                new Point(0,0),
                new Point(0,1),
                new Point(0,2),
                new Point(1,0)
        ), Turnable.Full, Player.NEUTRAL);
    }
}
