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

        List<Point> emptyFields = getEmptyPoints();

        List<Area> areas = getAreas(color, emptyFields);


        List<Area> sortedAreas = areas.stream().sorted(Comparator.comparingInt(Area::getAreaSize)).toList();
        while (sortedAreas.size() > 1) {
            Area a = sortedAreas.get(0);
            sortedAreas = sortedAreas.stream().filter(area -> {
                Point p = area.getArea().get(0);
                return !a.contains(p);
            }).toList();
            getConquerableArea(color, a);
        }

    }

    private List<Area> getAreas(PlayerColor color, List<Point> emptyIterations) {
        List<Area> areas = new ArrayList<>();
        while (emptyIterations.size() != 0) {
            final List<Point> reachableEmptyField = getReachableFields(color, emptyIterations.get(0));
            Area reachableEmptyArea = new Area(reachableEmptyField, reachableEmptyField.size());
            areas.add(reachableEmptyArea);
            emptyIterations = emptyIterations.stream().filter(point -> reachableEmptyField.stream().noneMatch(point1 -> point1.x == point.x && point.y == point1.y)).toList();
        }
        return areas;
    }


    private void getConquerableArea(PlayerColor color, Area area) {
        ArrayList<Point> pList = new ArrayList<>();
        if (area.isConquerable(game.getPreviousMoves(), color)) {
            conquerArea(color, area.getArea().get(0), pList);
        }
    }

    private void conquerArea(PlayerColor color, Point p, List<Point> allPoints) {
        //wenn das feld feld nicht dem spieler gehört, dann:
        if (isOutOfBounds(p)) {
            return;
        }
        for (Point p1 : allPoints) {
            if (p1.x == p.x && p1.y == p.y) {
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

    private List<Point> getReachableFields(PlayerColor color, Point p) {
        List<Point> queue = new ArrayList<>();
        List<Point> emptyPoints = new ArrayList<>();
        List<Point> donePoints = new ArrayList<>();
        queue.add(p);
        donePoints.add(p);

        while (queue.size() != 0) {
            Point newP = queue.get(0);
            queue.remove(newP);
            if (getContent(newP) == FieldContent.getOccupiedByPlayer(color)) {
                continue;
            }


            FieldContent f = getContent(newP);
            if (f == FieldContent.EMPTY ||
                    f == FieldContent.CATHEDRAL ||
                    color.value == PlayerColor.BLACK.value && f != FieldContent.BLACK_OCCUPIED ||
                    color.value == PlayerColor.WHITE.value && f != FieldContent.WHITE_OCCUPIED
            ) {
                emptyPoints.add(newP);
            }

            Point[] points = {
                    new Point(newP.x - 1, newP.y - 1), new Point(newP.x - 1, newP.y), new Point(newP.x - 1, newP.y + 1),
                    new Point(newP.x, newP.y - 1), new Point(newP.x, newP.y + 1),
                    new Point(newP.x + 1, newP.y - 1), new Point(newP.x + 1, newP.y), new Point(newP.x + 1, newP.y + 1),};

            List<Point> realisticPoints = Arrays.stream(points).filter(point -> !isOutOfBounds(point)).toList();
            List<Point> filteredNewPoints = realisticPoints.stream().filter(point -> donePoints.stream().noneMatch(point1 -> point.x == point1.x && point.y == point1.y)).toList();

            donePoints.addAll(filteredNewPoints);
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
        return p.x >= pageLength || p.x < 0 || p.y >= pageLength || p.y < 0;
    }

    private List<Point> getEmptyPoints() {
        return getAllPoints().stream().filter(point -> getContent(point).getValue() == FieldContent.EMPTY.getValue()).toList();
    }

    public int getCapturedArea(PlayerColor color) {
        return (int) Arrays.stream(content).filter(fieldContent -> color == PlayerColor.BLACK && fieldContent == FieldContent.BLACK_TERRITORY || color == PlayerColor.WHITE && fieldContent == FieldContent.WHITE_TERRITORY).count();
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