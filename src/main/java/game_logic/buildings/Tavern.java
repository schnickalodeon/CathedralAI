package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Turnable;

import java.awt.*;

public class Tavern extends Building {
    public Tavern(Player player) {
        super("Tavern", Turnable.Not, player, new Point(0, 0));
    }
}
