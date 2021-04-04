package game_logic;

import game_logic.buildings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Building {
    private final String name;
    private final Shape shape;
    private final Turnable turnable;
    private final PlayerColor playerColor;

    protected Building(String name, Shape shape, Turnable turnable, PlayerColor playerColor) {
        this.name = name;
        this.shape = shape;
        this.turnable = turnable;
        this.playerColor = playerColor;
    }

    public static List<Building> getAllBuildingsForPlayer(Player player) {

        PlayerColor color = player.getColor();

        List<Building> buildings = new ArrayList<>();
        buildings.add(new Tavern(color));
        buildings.add(new Tavern(color));

        buildings.add(new Stable(color));
        buildings.add(new Stable(color));

        buildings.add(new Inn(color));
        buildings.add(new Inn(color));

        buildings.add(new Bridge(color));
        buildings.add(new Manor(color));
        buildings.add(new Square(color));
        buildings.add(new Abbey(color));
        buildings.add(new Infirmary(color));
        buildings.add(new Castle(color));
        buildings.add(new Tower(color));

        return buildings;
    }

    public List<Point> getPoints(Point position) {
        return shape.getPoints(position);
    }

}
