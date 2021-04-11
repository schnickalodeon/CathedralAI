package game_logic;

import ai.AI;

public class PlayerBlack extends Player{
    public PlayerBlack(String name, Board board, AI ai) {
        super(PlayerColor.BLACK, name, board, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
}
