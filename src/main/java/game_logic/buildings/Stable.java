package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.Turnable;

import java.awt.*;

public class Stable extends Building {
    public Stable(Player player) {
        super("Stable", Turnable.Half, player,
                new Point(0, 0),
                new Point(1, 0));
    }
}
