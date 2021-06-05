package game_logic;

import ai.ArtificialIntelligent;

import java.util.ArrayList;

public class PlayerWhite extends Player {
    public PlayerWhite(String name, Game game,  ArtificialIntelligent artificialIntelligent) {
        super(PlayerColor.WHITE, name, game, artificialIntelligent);
        buildings = Building.getAllBuildingsForPlayer(this);
    }
    public PlayerWhite(Player white, Game game)
    {
        super(PlayerColor.WHITE, white.name, game, white.artificialIntelligent);
        this.buildings = new ArrayList<Building>(white.buildings);
    }

}
