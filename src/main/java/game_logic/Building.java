package game_logic;

import game_logic.buildings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Building {
    protected String name;
    protected Turnable turnable;
    protected PlayerColor playerColor;
    protected List<Point> points;

    public FieldContent getContent() {
        return FieldContent.getOccupiedByPlayer(playerColor);
    }

    protected Building(String name, Turnable turnable, PlayerColor color) {
        this.name = name;
        this.turnable = turnable;
        this.playerColor = color;
    }

    public Integer getSize() {
        return points.size();
    }

    protected Building(String name, Turnable turnable, Player player, Point... point) {
        this.name = name;
        points = Arrays.asList(point);
        this.turnable = turnable;
        this.playerColor = player.getColor();
    }

    protected void addPointToShape(int x, int y) {
        points.add(new Point(x, y));
    }

    protected int getNumPointsOfShape() {
        return points.size();
    }

    public static List<Building> getAllBuildingsForPlayer(Player player) {

        List<Building> buildings = new ArrayList<>();
        buildings.add(new Tavern(player));
        buildings.add(new Tavern(player));

        buildings.add(new Stable(player));
        buildings.add(new Stable(player));

        buildings.add(new Inn(player));
        buildings.add(new Inn(player));

        buildings.add(new Bridge(player));
        buildings.add(new Manor(player));
        buildings.add(new Square(player));
        buildings.add(new Infirmary(player));
        buildings.add(new Castle(player));
        buildings.add(new Tower(player));

        buildings.add(new Academy(player));
        buildings.add(new Abbey(player));

        if (player.getColor() == PlayerColor.WHITE) {
            buildings.add(new Cathedral());
        }

        return buildings;
    }

    public List<Point> getShape(Direction direction) {
        return turn(direction);
    }

    public int getTurnable() {
        return turnable.getValue();
    }

    public List<Point> turn(Direction direction) {
        List<Point> rotated = new ArrayList<>();
        points.forEach(p -> {
            Point turnedPoint = p;
            for (int turns = 0; turns < direction.getNumber() % turnable.getValue(); turns++) {
                turnedPoint = new Point(-turnedPoint.y, turnedPoint.x);
            }
            rotated.add(turnedPoint);
        });
        return rotated;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;

        return building.name.equals(this.name) && building.playerColor.equals(this.playerColor);
    }

    @Override
    public int hashCode() {

        int nameASCISum = 0;

        for (char c : name.toCharArray()) {
            nameASCISum += Character.getNumericValue(c);
        }

        final int prime = 31;
        int result = 1;
        result = prime * result + nameASCISum;
        return result;
    }
}
