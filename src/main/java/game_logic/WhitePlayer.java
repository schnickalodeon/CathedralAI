package game_logic;

import ai.AI;

public class WhitePlayer extends Player {
    public WhitePlayer(String name, Board board, AI ai) {
        super(PlayerColor.WHITE, name, board, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }




}
