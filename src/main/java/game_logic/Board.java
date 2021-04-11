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


    public void setContent(List<Point> points, FieldContent content)
    {
        points.forEach(p ->{
            int index = getIndexByPoint(p);
            this.content[index] = content;
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

    public String getBoardHtml(int turn){
        StringBuilder sb = new StringBuilder();
        String tableStyle = "\"min-width:300px; min-height:300px; border: 1px solid black; margin-bottom:50px;\">";
        sb.append("<table style=");
        sb.append(tableStyle);
        sb.append("<h3>");
        sb.append("Turn ");
        sb.append(turn);
        sb.append("</h3>");

        int pageLength = (int) Math.sqrt(FIELD_COUNT);

        int index;
        for (int row = 0; row < pageLength; row++ ){
            sb.append("<tr>");
            for (int col = 0; col < pageLength; col++ ){
                index = getIndexByCoordinates(col,row);
                Color color = FieldContent.getColor(content[index]);
                String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(),color.getBlue());
                String style = "style=\"background-color:" + hexColor + ";\">";
                sb.append("<td ");
                sb.append(style);
                sb.append("</td>");
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
