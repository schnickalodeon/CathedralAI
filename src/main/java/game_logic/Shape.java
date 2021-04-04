package game_logic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {
    private final Point[] points;

    public Shape(Point ...points) {
        this.points = points;
    }

    public List<Point> getPoints(Point position){
        List<Point> boardIndices = new ArrayList<>();

        for (Point point: points) {
            //TODO Check auf Drehung!
            int newX = point.x + position.x;
            int newY = point.y + position.y;
            boardIndices.add(new Point(newX,newY));
        }

        return boardIndices;
    }

}
