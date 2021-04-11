package game_logic;

import ai.AI;

public class PlayerBlack extends Player{
    public PlayerBlack(String name, Game game, AI ai) {
        super(PlayerColor.BLACK, name, game, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }

    @Override
    public Move getFirstMove() throws Exception {
        throw new Exception("BLaCK nOt fIrSt mOvE !!!!");
    }
}
