package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Turnable;

import java.awt.*;

public class Square extends Building {
    public Square(Player player) {
        super("Square", Turnable.Not, player, new Point(0,0), new Point(0,1), new Point(1,0), new Point(1,1));
    }
}
