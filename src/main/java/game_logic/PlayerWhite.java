package game_logic;

import ai.AI;

public class PlayerWhite extends Player {
    public PlayerWhite(String name, Board board, AI ai) {
        super(PlayerColor.WHITE, name, board, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }




}
