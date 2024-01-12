package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Turnable;

import java.awt.*;

public class Tower extends Building {
    public Tower(Player player) {
        super("Tower"
                , Turnable.Full, player,
                new Point(-1, -1),
                new Point(0, -1),
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1));
    }
}
