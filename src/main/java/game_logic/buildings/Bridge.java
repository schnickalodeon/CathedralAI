package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Shape;
import game_logic.Turnable;

import java.awt.*;

public class Bridge extends Building {
    public Bridge(Player player) {
        super("Bridge", new Shape(new Point(0,0), new Point(0,1), new Point(0,2)), Turnable.Half, player);
    }
}
