package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;
import java.util.ArrayList;

public class Academy extends Building {
    public Academy(Player player) {
        super("Academy", Turnable.Full, player);
        this.points = new ArrayList<>();
        //Black
        if(player.getColor() == PlayerColor.BLACK){
            this.addPointToShape(-1,0);
            this.addPointToShape(0,-1);
            this.addPointToShape(0,0);
            this.addPointToShape(0,1);
            this.addPointToShape(1,-1);
        }
        else{
            //White
            this.addPointToShape(-1,0);
            this.addPointToShape(0,-1);
            this.addPointToShape(0,0);
            this.addPointToShape(0,1);
            this.addPointToShape(1,1);
        }

    }
}
