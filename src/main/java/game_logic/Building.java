package game_logic;

import game_logic.buildings.Tavern;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Building {
    private final String name;
    private final Shape shape;
    private final Turnable turnable;
    private final Player player;

    public Building(String name, Shape shape, Turnable turnable, Player player) {
        this.name = name;
        this.shape = shape;
        this.turnable = turnable;
        this.player = player;
    }

    public List<Building> getDefaultBildings(Player player) {
        List<Building> buildings = new ArrayList<>();
        buildings.add(new Tavern(player));

        return buildings;
    }

}
