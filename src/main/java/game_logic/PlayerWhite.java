package game_logic;

import ai.AI;

import java.util.ArrayList;
import java.util.List;

public class PlayerWhite extends Player {
    public PlayerWhite(String name, Game game,  AI ai) {
        super(PlayerColor.WHITE, name, game, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
    public PlayerWhite(Player white, Game game)
    {
        super(PlayerColor.WHITE, white.name, game, white.ai);
        this.buildings = new ArrayList<Building>(white.buildings);
    }

    public Move getFirstMove(){
        return ai.getFirstMove(game.getBoard(),this);
    }




}
