package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Castle extends Building {
    public Castle(PlayerColor playerColor) {
        super("Castle",
                Turnable.Full, playerColor,
                new Point(-1,0),
                new Point(-1,1),
                new Point(0,0),
                new Point(1,0),
                new Point(1,1));
    }
}
