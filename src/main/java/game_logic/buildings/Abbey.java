package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Shape;
import game_logic.Turnable;

import java.awt.*;

public class Abbey extends Building {
    public Abbey(PlayerColor playerColor) {
        super("Abbey", new Shape(new Point(-1,0), new Point(0,0), new Point(0,1), new Point(1,1)), Turnable.Half, playerColor);
    }
}
