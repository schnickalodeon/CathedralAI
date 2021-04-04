package game_logic;

import game_logic.buildings.Player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Move {
    private final int moveId;
    private final Point position;
    private final Building building;
    private final Direction direction;
    private final Player player;

    public Move(int moveId ,Point position, Building building, Direction direction, Player player) {
        this.position = position;
        this.building = building;
        this.direction = direction;
        this.moveId = moveId;
        this.player = player;
    }

    public List<Point> getOccupyingPoints() {
        return building.getPoints(position);
    }
}
