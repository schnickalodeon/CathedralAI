package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Bridge extends Building {
    public Bridge(PlayerColor playerColor) {
        super("Bridge",
                Turnable.Half, playerColor,new Point(0,0), new Point(0,1), new Point(0,2));
    }
}
