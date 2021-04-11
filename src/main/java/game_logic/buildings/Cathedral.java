package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;
import java.util.ArrayList;

public class Cathedral extends Building {
    public Cathedral() {
        super("Cathedral",Turnable.Full,PlayerColor.NEUTRAL);
        points = new ArrayList<>();
        this.addPointToShape(-1,0);
        this.addPointToShape(0,-1);
        this.addPointToShape(0,0);
        this.addPointToShape(0,1);
        this.addPointToShape(0,2);
        this.addPointToShape(1,0);
    }
}
