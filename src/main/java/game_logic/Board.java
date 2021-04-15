package game_logic;

import java.awt.*;
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
    //TODO needs REFACTORING. A LOT
    public void checkArea(Move move)
    {
        for(Point p: move.getOccupyingPoints())
        {
            //TODO If you enclose two or more buildings. one of which may be the Cathedral,
            // then none of the buildings may be removed and
            // the space is still available to your opponent
            List<Point> allPoints = new ArrayList<>();

            int iteration = AllNonColoredPositions(move.getPlayer().getColor());
            int recursive = recursiveGetSurroundingPoints(p, allPoints,move.getPlayer().getColor());

            //how big are the areas? is there a solution that borders both areas? always splits into 2? need to think about it
            int area1 = -1;
            int area2 = -1;

            //also fixes if is placed within a captured area
            if(recursive ==0) continue;

            // if the number of fields you can reach without going through fields of your color, nothing has been occupied
            //this should happen most of the time
            if (iteration == recursive)
            {
                //this solution is bad, since this can only be true if another field was unequal before,
                //but I cant think off a clever solution to this problem.
                if (area1 != -1)
                {
                    recursiveColorSurroundingFields(p, allPoints, move.getPlayer().getColor());
                }
                System.out.println("equal: "+recursive);
            }
            //if you cannot do that, the area is being split into 2 pieces, check which one is smaller; that one is conquered.
            //double checked for rules
            else
            {
                Point tmp = p;
                System.out.println("unequal");
                System.out.println("Recursive: "+recursive);
                System.out.println("iterated: "+iteration);
                if (area1==-1)
                {
                    area1=recursive;
                }
                //Check if there is already 1 area. we need 2 to make sure we get the right field
                else if (recursive != area1)
                {
                    area2 = recursive;
                    if (area1 > area2)
                    {
                        //always pick the one that neighbors more fields.
                        if( area2 < area1)
                        {
                            recursiveColorSurroundingFields(tmp, allPoints, move.getPlayer().getColor());
                        }
                        else
                        {
                            recursiveColorSurroundingFields(p, allPoints, move.getPlayer().getColor());
                        }
                        return;
                    }
                }
            }
        }
    }

    //TODO color all the fields, that are surrounded
    private void recursiveColorSurroundingFields(Point p, List<Point> allPoints, PlayerColor color)
    {

    }

    //for a given point get every surrounding point, and check if its NOT the playerCol (for checking area)
    private int recursiveGetSurroundingPoints(Point p, List<Point> allPoints, PlayerColor playercol)
    {
        //TODO this code is absolutely terrible. ABSOLUTELY NEEDS REFACTORING

        //for every neighboring point
        getSurroundingTile(new Point(p.x-1,p.y-1),allPoints,playercol);
        getSurroundingTile(new Point(p.x,p.y-1),allPoints,playercol);
        getSurroundingTile(new Point(p.x+1,p.y-1),allPoints,playercol);
        getSurroundingTile(new Point(p.x-1,p.y),allPoints,playercol);
        getSurroundingTile(new Point(p.x+1,p.y),allPoints,playercol);
        getSurroundingTile(new Point(p.x-1,p.y+1),allPoints,playercol);
        getSurroundingTile(new Point(p.x,p.y+1),allPoints,playercol);
        getSurroundingTile(new Point(p.x+1,p.y+1),allPoints,playercol);
        return allPoints.size();
    }

    //making the terrible code less... terrible.
    private void getSurroundingTile(Point p, List<Point> allPoints, PlayerColor playercol)
    {
        if(//is it stil inbounds?
             p.x < 10 && p.y < 10 &&
        p.x >= 0 && p.y >= 0)

        {
            //is it occupied by the player?
            if (getContent(getIndexByCoordinates(p.x, p.y)) == FieldContent.EMPTY ||
                    getContent(getIndexByCoordinates(p.x,p.y))== FieldContent.CATHEDRAL)
            {
                //is it already in the list?
                for(Point point: allPoints)
                {
                    if(point.x == p.x && point.y == p.y)
                    {
                        return;
                    }
                }
                //add the point to the list and get all surrounding points for this point.
                allPoints.add(new Point(p.x , p.y ));
                recursiveGetSurroundingPoints(allPoints.get(allPoints.size() - 1), allPoints, playercol);
            }
        }
    }

    //iterate through every field and check all empty fields. this would work better if things would be Colored.
    private int AllNonColoredPositions(PlayerColor playerCol) {
        int numFields =0;

        for (int i=0; i<FIELD_COUNT; i++)
        {
            if (getContent(i) == FieldContent.EMPTY || getContent(i)== FieldContent.CATHEDRAL)
            {
                numFields++;
            }
        }
        return numFields;
    }
}