package game_logic;

import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board
{
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

    public void setContent(Point point, FieldContent content)
    {
            int index = getIndexByPoint(point);
            this.content[index] = content;
    }

    public FieldContent getContent(Point p) {
        int index = getIndexByPoint(p);
        return content[index];
    }

    public FieldContent getContent(int index) {
        return content[index];
    }

    private int getIndexByPoint(Point p) {

        int pageLength = Math.round((float)Math.sqrt(FIELD_COUNT));
        assert p.x < pageLength && p.x >= 0;
        assert p.y < pageLength && p.y >= 0;
        return pageLength * p.y + p.x;
    }

    private Point getPointByIndex(int i)
    {
        int x = i%10;
        int y = i/10;
        return new Point(x,y);
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
        sb.append(turn+1);
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

    //checking bounds
    public boolean isOutOfBounds(Point p)
    {
        int pageLength = Math.round((float) Math.sqrt(FIELD_COUNT));
        return p.x >= pageLength || p.x < 0 ||
                p.y >= pageLength || p.y < 0;
    }

    //check if the board has any area that needs to be owned by one of the players.
    //move may not be necessary
    public void checkArea(PlayerColor color)
    {
        int emptyFieldCount;
        int reachbleFieldCount;
        ArrayList<Point> emptyFields = getEmptyPoints();
        ArrayList<Point> emptyFieldsRecursive = new ArrayList<>();
        emptyFieldCount = emptyFields.size();
        ArrayList<Integer> numberPointsInArea =  new ArrayList<>();
        ArrayList<Point> pointsToAccess = new ArrayList<>();

        for (Point p: emptyFields)
        {
            //man merke sich immer den höchsten wert erreichbarer felder für jeden punkt
            //wenn wenn erreichbare felder > höchster wert  && > 0 -> färbe ein.
            //benachbarte felder rekursiv ebenfalls "einnehmen" bis grenzen erreicht werden.
            //empty fields muss verkleinert werden um die anzahl eingenommener punkte
            emptyFieldsRecursive.clear();
            reachbleFieldCount = getReachableFields(color,p, emptyFieldsRecursive, 0);
            if(reachbleFieldCount == emptyFieldCount)
            {
                return;
            }
            //punkte müssen eingenommen werden
            else
            {
                for (Point point: emptyFieldsRecursive)
                {
                    if(pointsToAccess.size() > 0) {
                        if (point.x == pointsToAccess.get(0).x && point.y == pointsToAccess.get(0).y)
                        {
                            break;
                        }
                        if (numberPointsInArea.get(0) != reachbleFieldCount)
                        {
                            pointsToAccess.add(point);
                            numberPointsInArea.add(reachbleFieldCount);
                        }
                    }
                    else
                    {
                        pointsToAccess.add(point);
                        numberPointsInArea.add(reachbleFieldCount);
                    }
                    if (pointsToAccess.size() ==2)
                    {
                        ArrayList<Point> pList = new ArrayList<>();
                        if(numberPointsInArea.get(0) > numberPointsInArea.get(1))
                        {
                            conquerArea(color, pointsToAccess.get(1),pList);
                            emptyFieldCount -= numberPointsInArea.get(1);
                            numberPointsInArea.remove(1);
                            pointsToAccess.remove(1);
                        }
                        else
                        {
                            conquerArea(color, pointsToAccess.get(0),pList);
                            emptyFieldCount -= numberPointsInArea.get(0);
                            numberPointsInArea.remove(0);
                            pointsToAccess.remove(0);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void conquerArea(PlayerColor color, Point p, List<Point> allPoints)
    {
        //wenn das feld feld nicht dem spieler gehört, dann:
        if(isOutOfBounds(p))
        {
            return;
        }
        for (Point p1: allPoints) {
            if (p1.x == p.x && p1.y == p.y) {
                return;
            }
        }

        allPoints.add(p);
        if(getContent(p) == FieldContent.getOccupiedByPlayer(color))
        {
            return;
        }
        /*TODO wenn aus der move liste 2 moves in diesem bereich gemacht wurden, (was mit koordinatenabfrage gut möglich ist) mache alle felder,
        die NICHT der spielerfarbe entsprechen eingenommen.
        remove die Figur vom Board, und gebe sie dem Spieler wieder. */
        if(true)
        {
            if (color == PlayerColor.BLACK)
            {
                setContent(p, FieldContent.BLACK_TERRITORY);
            }
            else
            {
                setContent(p, FieldContent.WHITE_TERRITORY);
            }
            conquerArea(color, new Point (p.x-1,p.y-1),allPoints);
            conquerArea(color, new Point (p.x,p.y-1),allPoints);
            conquerArea(color, new Point (p.x+1,p.y-1),allPoints);
            conquerArea(color, new Point (p.x-1,p.y),allPoints);
            conquerArea(color, new Point (p.x+1,p.y),allPoints);
            conquerArea(color, new Point (p.x-1,p.y+1),allPoints);
            conquerArea(color, new Point (p.x,p.y+1),allPoints);
            conquerArea(color, new Point (p.x+1,p.y+1),allPoints);
        }
    }

    private int getReachableFields(PlayerColor color, Point p, List<Point> allPoints, int counter)
    {
        //wenn das feld feld nicht dem spieler gehört, dann:
        if(isOutOfBounds(p))
        {
            return counter;
        }
        
        //es ist ein valider punkt, überprüfe, ob der punkt bereits besucht wurde.
        for (Point p1: allPoints)
        {
            if (p1.x == p.x && p1.y == p.y)
            {
                return counter;
            }
        }
        allPoints.add(p);
        
        //ist das feld von dem Spieler occupied.
        if(getContent(p) == FieldContent.getOccupiedByPlayer(color))
        {
            return counter;
        }
        
        if(getContent(p)== FieldContent.EMPTY)
        {
            counter++;
        }
        
        //TODO endrekursiv schreiben für performance
        counter = getReachableFields(color, new Point (p.x-1,p.y-1),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x,p.y-1),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x+1,p.y-1),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x-1,p.y),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x+1,p.y),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x-1,p.y+1),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x,p.y+1),allPoints,counter);
        counter = getReachableFields(color, new Point (p.x+1,p.y+1),allPoints,counter);
        return counter;
    }


    //iterate through every field and check all empty fields. this would work better if things would be Colored.
    private ArrayList<Point> getEmptyPoints() {
        ArrayList <Point> emptyPoints = new ArrayList<>();
        for (int i=0; i < FIELD_COUNT; i++)
        {
            if(getContent(i)== FieldContent.EMPTY)
            {
                emptyPoints.add(getPointByIndex(i));
            }
        }
        return emptyPoints;
    }
}