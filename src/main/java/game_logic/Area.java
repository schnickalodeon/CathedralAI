package game_logic;

import game_logic.buildings.Cathedral;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Area
{
    private ArrayList<Point> points;
    private int numFields;

    public Area(ArrayList<Point> points_, int reachbleEmptyFieldCount)
    {
        points = points_;
        numFields = reachbleEmptyFieldCount;
    }

    public int getAreaSize()
    {
        return numFields;
    }
    public ArrayList<Point> getArea(){return points;}
    boolean contains(Point p)
    {
        for (Point point : points)
        {
            if (point.x == p.x && point.y == p.y) return true;
        }
        return false;
    }

    public boolean isConquerable(ArrayList<Move> moves, PlayerColor color)
    {
        return checkForMovesIn(moves,color) <= 2;}

    int checkForMovesIn(ArrayList<Move> moves,  PlayerColor color)
    {
        ArrayList<Move> movesInArea=  new ArrayList<>();
        for (int i=0; i<moves.size();i++) {
            if (contains(moves.get(i).getPosition()) && color != moves.get(i).getPlayer().getColor()||
            contains(moves.get(i).getPosition()) && moves.get(i).getBuilding().getSize() == 6) {
                movesInArea.add(moves.get(i));
            }
        }

            if (movesInArea.size() == 1 && !(movesInArea.get(0).getBuilding()== moves.get(0).getBuilding()))
            {
                movesInArea.get(0).revert();
            }
        return movesInArea.size();
    }
}