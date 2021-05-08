package game_logic;

import ai.AI;

import java.util.ArrayList;

public class PlayerBlack extends Player{
    public PlayerBlack(String name, Game game, AI ai) {
        super(PlayerColor.BLACK, name, game, ai);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
    public PlayerBlack(Player black, Game game)
    {
        super(PlayerColor.BLACK, black.name, game, black.ai);
        this.buildings = new ArrayList<Building>(black.buildings);
    }

    @Override
    public Move getFirstMove() throws Exception {
        throw new Exception("BLaCK nOt fIrSt mOvE !!!!");
    }
}