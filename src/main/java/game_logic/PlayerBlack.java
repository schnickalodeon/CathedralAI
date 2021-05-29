package game_logic;

import ai.ArtificialIntelligent;

import java.util.ArrayList;

public class PlayerBlack extends Player{
    public PlayerBlack(String name, Game game, ArtificialIntelligent artificialIntelligent) {
        super(PlayerColor.BLACK, name, game, artificialIntelligent);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
    public PlayerBlack(Player black, Game game)
    {
        super(PlayerColor.BLACK, black.name, game, black.artificialIntelligent);
        this.buildings = new ArrayList<Building>(black.buildings);
    }

    @Override
    public Move getFirstMove() throws Exception {
        throw new Exception("BLaCK nOt fIrSt mOvE !!!!");
    }
}