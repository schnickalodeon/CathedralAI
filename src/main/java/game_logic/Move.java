package game_logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Point position;
    private final Building building;
    private final Direction direction;
    private final Player player;

    public Player getPlayer() {
        return player;
    }

    public Move(Point position, Building building, Direction direction, Player player) {
        this.position = position;
        this.building = building;
        this.direction = direction;
        this.player = player;
    }

    public List<Point> getOccupyingPoints() {

        List<Point> shape =  new ArrayList<Point>();
        building.getShape(direction).forEach(p ->
        {
            shape.add(new Point(p.x,p.y));
        });
        shape.forEach(p ->
        {
            p.x += position.x;
            p.y += position.y;
        });
        return shape;
    }

    public Building getBuilding() { return building; }

    public String toString()
    {
        return "building: " + building.toString() + " " + player.toString() + " x:" + position.x + " y:" + position.y;
    }
}
