package game_logic;

import java.awt.Point;
import java.util.List;


public class Board {
    private static final int FIELDCOUNT = 100;
    public Board() {
        content = new FieldContent[FIELDCOUNT];
    }

    private static FieldContent[] content;

    public boolean place(Move move) {
        if(!isPlaceable(move)){
            return false;
        }

        //TODO hier Place

        return true;
    }

    public boolean isPlaceable(Move move){
        List<Point> points = move.getOccupyingPoints();

        for (Point point: points) {
            FieldContent fieldContent = getContent(point.x, point.y);
            if(fieldContent != FieldContent.EMPTY){
                return false;
            }
        }
        return true;
    }

    private FieldContent getContent(int x, int y) {
        int index = getIndexByCordiates(x, y);
        return content[index];
    }

    private int getIndexByCordiates(int x, int y) {
        assert x * y <= FIELDCOUNT;

        int sidelenght = (int) Math.sqrt(FIELDCOUNT);
        return sidelenght * y + x;
    }

}
