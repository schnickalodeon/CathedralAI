package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Tavern extends Building {
    public Tavern(PlayerColor playerColor) {
        super("Tavern", Turnable.Not, playerColor,new Point(0,0));
    }
}
