package game_logic;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class Board {
    private static final int FIELD_COUNT = 100;
    private  FieldContent[] content = new FieldContent[FIELD_COUNT];

    public Board()
    {
        Arrays.fill(content,FieldContent.EMPTY);
        System.out.println("end board constructor!");
    }


    public void setContent(List<Point> points, Player player)
    {
        FieldContent playerContent = FieldContent.getOccupiedByPlayer(player);
        points.forEach(p ->{
            int index = getIndexByPoint(p);
            this.content[index] = playerContent;
        });
    }

    public FieldContent getContent(Point p) {
        int index = getIndexByPoint(p);
        return content[index];
    }

    private int getIndexByPoint(Point p) {

        int pageLength = Math.round((float)Math.sqrt(FIELD_COUNT));
        assert p.x < pageLength && p.x >= 0;
        assert p.y < pageLength && p.y >= 0;
        return pageLength * p.y + p.x;
    }


    private int getIndexByCoordinates(int x, int y)
    {
        return getIndexByPoint(new Point(x,y));
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

    public boolean isOutOfBounds(Point p)
    {
        int pageLength = Math.round((float) Math.sqrt(FIELD_COUNT));
        return p.x >= pageLength || p.x < 0 ||
                p.y >= pageLength || p.y < 0;
    }
}
