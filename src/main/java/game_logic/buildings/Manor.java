package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Manor extends Building {
    public Manor(PlayerColor playerColor) {
        super("Manor",
                Turnable.Full, playerColor,
                        new Point(0,0),
                        new Point(-1,0),
                        new Point(1,0),
                        new Point(0,1));
    }
}
