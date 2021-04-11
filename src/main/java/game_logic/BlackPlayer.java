package game_logic;

import ai.AI;

public class BlackPlayer extends Player{
    public BlackPlayer(String name, Board board, AI ai) {
        super(PlayerColor.BLACK, name, board, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
}
