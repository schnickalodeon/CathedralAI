package game_logic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board
{
    private static final int FIELD_COUNT = 100;
    private  FieldContent[] content = new FieldContent[FIELD_COUNT];
    private Game game;

    public Board(Game game_)
    {
        Arrays.fill(content,FieldContent.EMPTY);
        System.out.println("end board constructor!");
        game = game_;
    }
    public Board(Board board)
    {
        content = new FieldContent[FIELD_COUNT];
        System.arraycopy(board.content,0,content,0,FIELD_COUNT);

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
        int reachbleEmptyFieldCount;

        //iterativ durch leere felder
        ArrayList<Point> emptyFields = getEmptyPoints();
        ArrayList<Point> reachableFromPoint = new ArrayList<>();
        emptyFieldCount = emptyFields.size();


        //todo Datenstruktur area erstellen
        ArrayList<Area> listOfAreas = new ArrayList<>();

        for (Point p: emptyFields)
        {
            //man merke sich immer den höchsten wert erreichbarer felder für jeden punkt
            //wenn wenn erreichbare felder > höchster wert  && > 0 -> färbe ein.
            //benachbarte felder rekursiv ebenfalls "einnehmen" bis grenzen erreicht werden.
            //empty fields muss verkleinert werden um die anzahl eingenommener punkte
            if (reachableFromPoint.contains(p))continue;
            reachableFromPoint.clear();

            reachbleEmptyFieldCount= getReachableFields(color,p, reachableFromPoint, 0);
            Area reachableEmptyArea = new Area(reachableFromPoint, reachbleEmptyFieldCount);
            boolean nothingToConquer = reachableEmptyArea.getAreaSize() == emptyFieldCount;
            if(nothingToConquer)
            {
                return;
            }
            //punkte müssen eingenommen werden
            else
            {
                getPartsToConquer(color, listOfAreas ,reachableEmptyArea, emptyFieldCount);
            }
        }
    }

    private void getPartsToConquer(PlayerColor color ,ArrayList<Area> listOfAreas,Area newArea, int emptyFieldCount)
    {
        //gibt es bereits einen Punkt auf den ich mich beziehe? haben wir bereits eine Area erkannt?
            if (listOfAreas.size() > 0) {

                //wenn sie keinen gemeinsamen punkt haben dann:
                if (!listOfAreas.get(0).getArea().contains(newArea.getArea().get(0)))
                {
                    listOfAreas.add(new Area(new ArrayList<Point>(newArea.getArea()),newArea.getAreaSize()));
                }
            }
            //es wurde noch keine area erkannt; folglich kann diese Area gefahrlos hinzugefügt werden.
            else
            {
                listOfAreas.add(new Area(new ArrayList<Point>(newArea.getArea()),newArea.getAreaSize()));
            }
            //wir haben 2 Areas gefunden: folglich muss eine eingenommen werden.
            if (listOfAreas.size() ==2)
            {
                ArrayList<Point> pList = new ArrayList<>();
                if(listOfAreas.get(0).getAreaSize() >= listOfAreas.get(1).getAreaSize())
                {
                    if(listOfAreas.get(1).isConquerable(game.getPreviousMoves(),color) )
                    {


                        conquerArea(color, listOfAreas.get(1).getArea().get(0), pList);
                        emptyFieldCount -= listOfAreas.get(1).getAreaSize();
                        listOfAreas.remove(1);
                    }
                }
                else
                {
                    if(listOfAreas.get(0).isConquerable(game.getPreviousMoves(),color) )
                    {
                        conquerArea(color, listOfAreas.get(0).getArea().get(0), pList);
                        emptyFieldCount -= listOfAreas.get(0).getAreaSize();
                        listOfAreas.remove(0);
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
        int i=0;
        if(getContent(p)== FieldContent.EMPTY)
        {
            i++;
        }

        return getReachableFields(color, new Point (p.x-1,p.y-1),allPoints,counter)+
        getReachableFields(color, new Point (p.x,p.y-1),allPoints,counter)+
         getReachableFields(color, new Point (p.x+1,p.y-1),allPoints,counter)+
         getReachableFields(color, new Point (p.x-1,p.y),allPoints,counter)+
         getReachableFields(color, new Point (p.x+1,p.y),allPoints,counter)+
         getReachableFields(color, new Point (p.x-1,p.y+1),allPoints,counter)+
         getReachableFields(color, new Point (p.x,p.y+1),allPoints,counter)+
    getReachableFields(color, new Point (p.x+1,p.y+1),allPoints,counter)+i;
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

    public int getCapturedArea(PlayerColor color) {
        int capturedCount =0;
        for (FieldContent f : content)
        {
            if (color == PlayerColor.BLACK&& f==FieldContent.BLACK_TERRITORY
                    || color==PlayerColor.WHITE && f==FieldContent.WHITE_TERRITORY) capturedCount++;
        }
        return capturedCount;
    }
}