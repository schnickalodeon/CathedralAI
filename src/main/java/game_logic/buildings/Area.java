package game_logic.buildings;

import java.awt.*;
import java.util.ArrayList;

public class Area
{
    private ArrayList<Point> points;

    public Area(ArrayList<Point> points_)
    {
        points = points_;
    }

    public int getAreaSize()
    {
        return points.size();
    }

    boolean contains(Point p)
    {
        for (Point point: points)
        {
            if(point.x == p.x && point.y == p.y) return true;
        }
        return false;
    }
}
