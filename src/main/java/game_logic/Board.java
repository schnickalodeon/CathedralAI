package game_logic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;


public class Board {
    private static final int FIELD_COUNT = 100;
    private FieldContent[] content = new FieldContent[FIELD_COUNT];
    private final Game game;

    public Board(Game game_) {
        Arrays.fill(content, FieldContent.EMPTY);
        game = game_;
    }

    public Board(Board board, Game game_) {
        content = new FieldContent[FIELD_COUNT];
        game = game_;
        System.arraycopy(board.content, 0, content, 0, FIELD_COUNT);

    }


    public void checkArea(PlayerColor color) {
        List<Point> reachableEmptyField;

        //iterativ durch leere felder
        List<Point> emptyFields = getEmptyPoints();
        ArrayList<Point> reachableFromPoint = new ArrayList<>();
        ArrayList<Area> listOfAreas1 = new ArrayList<>();
        List<Point> emptyIterations = emptyFields;

        while (emptyIterations.size() != 0) {
            reachableEmptyField = getReachableFields(color, emptyIterations.get(0), reachableFromPoint);
            Area reachableEmptyArea = new Area(reachableEmptyField, reachableEmptyField.size());
            listOfAreas1.add(reachableEmptyArea);

            List<Point> finalReachableEmptyField = reachableEmptyField;
            emptyIterations = emptyIterations.stream().filter(
                            point -> finalReachableEmptyField.stream().noneMatch(
                                    point1 -> point1.x == point.x && point.y == point1.y))
                    .toList();
        }

        while (listOfAreas1.size() != 1) {
            Area a = listOfAreas1.stream().sorted(Comparator.comparingInt(Area::getAreaSize)).toList().get(0);
            listOfAreas1.remove(a);
            getPartsToConquer(color, listOfAreas1, a);
        }
    }

    private void getPartsToConquer(PlayerColor color, ArrayList<Area> listOfAreas, Area newArea) {
        if (listOfAreas.size() > 0) {
            //entweder sie sind identisch oder haben keine Überschneidung an punkten.
            if (!listOfAreas.get(0).getArea().contains(newArea.getArea().get(0))) {
                listOfAreas.add(new Area(new ArrayList<Point>(newArea.getArea()), newArea.getAreaSize()));
            }
        }
        //es wurde noch keine area erkannt; folglich kann diese Area gefahrlos hinzugefügt werden.
        else {
            listOfAreas.add(new Area(new ArrayList<Point>(newArea.getArea()), newArea.getAreaSize()));
        }
        //wir haben 2 Areas gefunden: folglich muss eine eingenommen werden.
        if (listOfAreas.size() == 2) {
            ArrayList<Point> pList = new ArrayList<>();
            if (listOfAreas.get(0).getAreaSize() >= listOfAreas.get(1).getAreaSize()) {
                if (listOfAreas.get(1).isConquerable(game.getPreviousMoves(), color)) {


                    conquerArea(color, listOfAreas.get(1).getArea().get(0), pList);
                    listOfAreas.remove(1);
                }
            } else {
                if (listOfAreas.get(0).isConquerable(game.getPreviousMoves(), color)) {
                    conquerArea(color, listOfAreas.get(0).getArea().get(0), pList);
                    listOfAreas.remove(0);
                }
            }
        }
    }

    private void conquerArea(PlayerColor color, Point p, List<Point> allPoints) {
        //wenn das feld feld nicht dem spieler gehört, dann:
        if (isOutOfBounds(p)) {
            return;
        }
        for (Point p1 : allPoints) {
            if (getContent(p1) == (color.name.equals("Black")? FieldContent.BLACK_TERRITORY: FieldContent.WHITE_TERRITORY)) {
                return;
            }
        }

        allPoints.add(p);
        if (getContent(p) == FieldContent.getOccupiedByPlayer(color)) {
            return;
        }

        if (color == PlayerColor.BLACK) {
            setContent(p, FieldContent.BLACK_TERRITORY);
        } else {
            setContent(p, FieldContent.WHITE_TERRITORY);
        }
        conquerArea(color, new Point(p.x - 1, p.y - 1), allPoints);
        conquerArea(color, new Point(p.x, p.y - 1), allPoints);
        conquerArea(color, new Point(p.x + 1, p.y - 1), allPoints);
        conquerArea(color, new Point(p.x - 1, p.y), allPoints);
        conquerArea(color, new Point(p.x + 1, p.y), allPoints);
        conquerArea(color, new Point(p.x - 1, p.y + 1), allPoints);
        conquerArea(color, new Point(p.x, p.y + 1), allPoints);
        conquerArea(color, new Point(p.x + 1, p.y + 1), allPoints);
    }

    private List<Point> getReachableFields(PlayerColor color, Point p, List<Point> allPoints) {
        List<Point> queue = new ArrayList<>();
        List<Point> emptyPoints = new ArrayList<>();
        List<Point> reachedPoints = new ArrayList<>();
        queue.add(p);
        while (queue.size() != 0) {
            Point newP = queue.get(0);
            queue.remove(newP);
            if (getContent(newP) == FieldContent.getOccupiedByPlayer(color)) {
                continue;
            }
            if (getContent(newP) == FieldContent.EMPTY) {
                emptyPoints.add(newP);
            }
            reachedPoints.add(newP);

            Point[] points = {
                    new Point(newP.x - 1, newP.y - 1),
                    new Point(newP.x - 1, newP.y),
                    new Point(newP.x - 1, newP.y + 1),
                    new Point(newP.x, newP.y - 1),
                    new Point(newP.x, newP.y),
                    new Point(newP.x, newP.y + 1),
                    new Point(newP.x + 1, newP.y - 1),
                    new Point(newP.x + 1, newP.y),
                    new Point(newP.x + 1, newP.y + 1),
            };

            List<Point> realisticPoints = Arrays.stream(points).filter(point -> !isOutOfBounds(point)).toList();
            List<Point> filteredNewPoints = realisticPoints.stream().filter(point -> queue.stream().noneMatch(point1 ->
                    point.x == point1.x && point.y == point1.y) &&
                    reachedPoints.stream().noneMatch(point1 -> point.x == point1.x && point.y == point1.y)
            ).toList();

            queue.addAll(filteredNewPoints);
        }
        return emptyPoints;
    }


    //Acceptable methods below.

    public void setContent(List<Point> points, FieldContent content) {
        points.forEach(p -> {
            int index = getIndexByPoint(p);
            this.content[index] = content;
        });
    }

    public void setContent(Point point, FieldContent content) {
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

        int pageLength = Math.round((float) Math.sqrt(FIELD_COUNT));
        assert p.x < pageLength && p.x >= 0;
        assert p.y < pageLength && p.y >= 0;
        return pageLength * p.y + p.x;
    }

    private Point getPointByIndex(int i) {
        int pageLength = Math.round((float) Math.sqrt(FIELD_COUNT));
        int x = i % pageLength;
        int y = i / pageLength;
        return new Point(x, y);
    }

    public boolean isOutOfBounds(Point p) {
        int pageLength = Math.round((float) Math.sqrt(FIELD_COUNT));
        return p.x >= pageLength || p.x < 0 ||
                p.y >= pageLength || p.y < 0;
    }

    private List<Point> getEmptyPoints() {
        return getAllPoints().stream().filter(point -> getContent(point).getValue() == FieldContent.EMPTY.getValue()).toList();
    }

    public int getCapturedArea(PlayerColor color) {
        return (int) Arrays.stream(content).filter(
                fieldContent -> color == PlayerColor.BLACK && fieldContent == FieldContent.BLACK_TERRITORY ||
                        color == PlayerColor.WHITE && fieldContent == FieldContent.WHITE_TERRITORY
        ).count();
    }

    public List<Point> getAllPoints() {
        return IntStream.range(0, content.length).mapToObj(this::getPointByIndex).toList();
    }

    private int getIndexByCoordinates(int x, int y) {
        return getIndexByPoint(new Point(x, y));
    }

    public String getBoardHtml(int turn) {
        StringBuilder sb = new StringBuilder();
        String tableStyle = "\"min-width:300px; min-height:300px; border: 1px solid black; margin-bottom:50px;\">";
        sb.append("<table style=");
        sb.append(tableStyle);
        sb.append("<h3>");
        sb.append("Turn ");
        sb.append(turn + 1);
        sb.append("</h3>");

        int pageLength = (int) Math.sqrt(FIELD_COUNT);

        int index;
        for (int row = 0; row < pageLength; row++) {
            sb.append("<tr>");
            for (int col = 0; col < pageLength; col++) {
                index = getIndexByCoordinates(col, row);
                Color color = FieldContent.getColor(content[index]);
                String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
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

}