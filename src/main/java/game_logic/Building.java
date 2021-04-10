package game_logic;

import game_logic.buildings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Building {
    private final String name;
    private final Turnable turnable;
    private final PlayerColor playerColor;
    private final List<Point> points;


    protected Building(String name, Turnable turnable, PlayerColor playerColor, Point...point) {
        this.name = name;
        points = Arrays.asList(point);
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

    public List<Point> getShape(Direction direction) {
        List<Point> rotated = turn(direction);
        return rotated;
    }

    protected List<Point> turn(Direction direction)
    {
        List<Point> rotated = new ArrayList<>();
        points.forEach(p->{
            Point turnedPoint = p;
            for( int turns =0 ; turns < direction.getNumber()%turnable.getValue(); turns++)
            {
                turnedPoint = new Point(-turnedPoint.y, turnedPoint.x);
            }
            rotated.add(turnedPoint);
        });
        return rotated;
    }


}
