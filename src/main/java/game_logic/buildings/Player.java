package game_logic.buildings;

import game_logic.Building;
import game_logic.Move;
import game_logic.PlayerColor;

import java.util.List;

public class Player {
    private final PlayerColor color;
    private final String name;
    private final List<Building> remainingBuildings;

    public PlayerColor getColor() {
        return color;
    }

    public Player(PlayerColor color, String name) {
        this.color = color;
        this.name = name;

        remainingBuildings = Building.getAllBuildingsForPlayer(this);
    }
}
