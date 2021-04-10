package game_logic;

import game_logic.buildings.Player;

import java.awt.Point;
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
        return building.getPoints(position, direction);
    }


    public void Place(Board board) {
        List<Point> points = this.getOccupyingPoints();
        FieldContent content = FieldContent.getOccupiedByPlayer(player);
        points.forEach(p -> board.setContent(p.x,p.y,content));
        player.removeBuildiung(building);
    }
}
