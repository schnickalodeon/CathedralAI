package game_logic;

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
        numFields = reachbleEmptyFieldCount;;
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

}
