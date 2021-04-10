package game_logic.buildings;

import game_logic.Building;
import game_logic.PlayerColor;
import java.util.List;

public class Player {
    private final PlayerColor color;
    private final String name;
    private final List<Building> buildings;

    public PlayerColor getColor() {
        return color;
    }
    public List<Building> getBuildings() { return buildings; }

    public Player(PlayerColor color, String name) {
        this.color = color;
        this.name = name;

        buildings = Building.getAllBuildingsForPlayer(this);
    }

    public void removeBuildiung(Building building){
        buildings.remove(building);
    }

    @Override
    public String toString() {
        return "Spieler " + name + "(" + color + ")";
    }
}
