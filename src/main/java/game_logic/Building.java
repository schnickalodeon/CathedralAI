package game_logic;

import game_logic.buildings.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Building {
    private final String name;
    private final Shape shape;
    private final Turnable turnable;
    private final Player player;

    protected Building(String name, Shape shape, Turnable turnable, Player player) {
        this.name = name;
        this.shape = shape;
        this.turnable = turnable;
        this.player = player;
    }

    public Map<Building, Integer> getAllBuildingsForPlayer(Player player) {
        Map<Building, Integer> buildings = new HashMap<>();
        buildings.put(new Tavern(player),2);
        buildings.put(new Stable(player),2);
        buildings.put(new Inn(player),2);

        buildings.put(new Bridge(player),1);
        buildings.put(new Manor(player),1);
        buildings.put(new Square(player),1);
        buildings.put(new Abbey(player),1);
        buildings.put(new Infirmary(player),1);
        buildings.put(new Castle(player),1);
        buildings.put(new Tower(player),1);

        return buildings;
    }

}
