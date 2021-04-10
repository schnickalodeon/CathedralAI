package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Stable extends Building {
    public Stable(PlayerColor playerColor) {
        super("Stable", Turnable.Half, playerColor,new Point(0,0),new Point(1,0));
    }
}
