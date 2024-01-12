package game_logic;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Area {
    private final ArrayList<Point> points;
    private final int numFields;

    public Area(List<Point> points_, int reachbleEmptyFieldCount) {
        points = new ArrayList<>();

        for (Point p : points_) {
            points.add(new Point(p.x, p.y));
        }
        numFields = reachbleEmptyFieldCount;
    }

    public int getAreaSize() {
        return numFields;
    }

    public ArrayList<Point> getArea() {
        return points;
    }

    boolean contains(Point p) {
        for (Point point : points) {
            if (point.x == p.x && point.y == p.y) return true;
        }
        return false;
    }

    public boolean isConquerable(ArrayList<Move> moves, PlayerColor color) {
        return checkForMovesIn(moves, color) <= 2;
    }

    int checkForMovesIn(ArrayList<Move> moves, PlayerColor color) {

        List<Move> movesInArea =  moves.stream().filter(move ->
                        this.contains(move.getPosition()) &&
                                color != move.getPlayer().getColor() ||
                                this.contains(move.getPosition()) &&
                                        move.getBuilding().getSize() == 6).toList();

        if (movesInArea.size() == 1 &&
                !(movesInArea.get(0).getBuilding() == moves.get(0).getBuilding())) {
            movesInArea.get(0).revert();
        }
        if(color.value == PlayerColor.BLACK.value) System.out.println(movesInArea.size());
        return movesInArea.size();
    }
}