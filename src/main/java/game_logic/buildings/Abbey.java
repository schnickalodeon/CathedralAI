package game_logic.buildings;

import game_logic.Building;
import game_logic.Player;
import game_logic.PlayerColor;
import game_logic.Turnable;

import java.awt.*;

public class Abbey extends Building {
    public Abbey(Player player) {
        super("Abbey", Turnable.Half, player);

        if(player.getColor() == PlayerColor.BLACK){
            this.addPointToShape(-1,0);
            this.addPointToShape(0,0);
            this.addPointToShape(0,1);
            this.addPointToShape(1,1);
        } else{
            //White
            this.addPointToShape(1,0);
            this.addPointToShape(0,0);
            this.addPointToShape(0,1);
            this.addPointToShape(-1,1);
        }


    }
}
