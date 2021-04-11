package game_logic;

import ai.AI;

public class PlayerWhite extends Player {
    public PlayerWhite(String name, Game game,  AI ai) {
        super(PlayerColor.WHITE, name, game, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }

    public Move getFirstMove(){
        return ai.getFirstMove(game.getBoard(),this);
    }




}
