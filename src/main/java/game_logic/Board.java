package game_logic;

import java.awt.*;
import java.util.List;


public class Board {
    private static final int FIELD_COUNT = 100;
    private  FieldContent[] content = new FieldContent[FIELD_COUNT];


    public void setContent(List<Point> points, Player player)
    {
        FieldContent playerContent = FieldContent.getOccupiedByPlayer(player);
        points.forEach(p ->{
            int index = getIndexByPoint(p);
            this.content[index] = playerContent;
        });
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

    public void setContent(int x, int y, FieldContent fieldContent) {
        int index = getIndexByCoordinates(x,y);
        this.content[index] = fieldContent;
    }

    private FieldContent getContent(int x, int y) {
        int index = getIndexByCoordinates(x, y);
        return content[index];
    }

    private int getIndexByCoordinates(int x, int y) {
        assert x * y <= FIELD_COUNT;

        int pageLength = (int) Math.sqrt(FIELD_COUNT);
        return pageLength * y + x;
    }

    public String getBoardHtml(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");

        int pageLength = (int) Math.sqrt(FIELD_COUNT);

        int index;
        for (int row = 0; row < pageLength; row++ ){
            sb.append("<tr>");
            for (int col = 0; col < pageLength; col++ ){
                index = getIndexByCoordinates(col,row);
                Color color = FieldContent.getColor(content[index]);
                String style = "\"style=background-color:" + color.toString() + ";\"";
                sb.append("<td ");
                sb.append(style);
                sb.append("></td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

}
