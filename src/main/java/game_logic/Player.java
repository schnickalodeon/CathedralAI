package game_logic;

import ai.ArtificialIntelligent;
import game_logic.buildings.Cathedral;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {
    protected final PlayerColor color;
    protected final String name;
    protected final Game game;
    protected final ArtificialIntelligent artificialIntelligent;
    protected List<Building> buildings = new ArrayList<>();
    protected List<Move> viableMoves = new ArrayList<>();
    protected int timeOuts = 0;

    final private int bufferInSeconds;

    public PlayerColor getColor() {
        return color;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    private boolean isFirstTurn() {
        return game.getTurnNumber() == 0 && color == PlayerColor.WHITE;
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public Game getGame() {
        return game;
    }

    protected Player(PlayerColor color, String name, Game game, ArtificialIntelligent artificialIntelligent) {
        this.color = color;
        this.name = name;
        this.game = game;
        this.artificialIntelligent = artificialIntelligent;
        this.bufferInSeconds = 2;
    }

    protected Player(Player player) {
        this.color = player.color;
        this.name = player.name;
        this.game = new Game(player.game);
        this.artificialIntelligent = player.artificialIntelligent;
        this.buildings = new ArrayList<>(player.buildings);
        this.viableMoves = new ArrayList<>(player.viableMoves);
        this.timeOuts = player.timeOuts;
        this.bufferInSeconds = player.bufferInSeconds;
    }

    public int countPoints() {
        return buildings.stream().mapToInt(b -> b.points.size()).sum();
    }

    public void removeBuildiung(Building building) {
        buildings.remove(building);
    }

    public Move getNextMove() {
        Building building = (isFirstTurn()) ? new Cathedral() : null;
        viableMoves = generateValidMoves(building);
        if (viableMoves.isEmpty()) {
            return null;
        }
        return artificialIntelligent.getMove(getBoard(), this);
    }

    public boolean makeMove(Move move) {
        if (!isPlaceable(move)) {
            return false;
        }

        List<Point> occupiedBoardPositions = move.getOccupyingPoints();
        FieldContent content = move.getBuilding().getContent();
        getBoard().setContent(occupiedBoardPositions, content);
        removeBuildiung(move.getBuilding());

        return true;
    }

    public boolean isPlaceable(Move move) {
        List<Point> points = move.getOccupyingPoints();

        for (Point point : points) {
            if (getBoard().isOutOfBounds(point)) {
                return false;
            }
            FieldContent fieldContent = getBoard().getContent(point);
            if (color == PlayerColor.BLACK) {
                if (fieldContent != FieldContent.EMPTY && fieldContent != FieldContent.BLACK_TERRITORY) {
                    return false;
                }
            } else {
                if (fieldContent != FieldContent.EMPTY && fieldContent != FieldContent.WHITE_TERRITORY) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Move> generateValidMoves(List<Building> buildings) {
        ArrayList<Move> ml = new ArrayList<>();
        buildings.forEach(b -> ml.addAll(generateValidMoves(b)));
        return ml;
    }

    public List<Move> generateValidMoves(Building building) {
        List<Move> ml = new ArrayList<>();
        for (int x = 0; x < Board.fieldSize; x++) {
            for (int y = 0; y < Board.fieldSize; y++) {
                //If moves for a specific Building should be generated, use only these.
                for (Building b : getPlaceableBuildings(building)) {
                    for (int r = 0; r < b.getTurnable(); r++) {
                        Move m = new Move(new Point(x, y), b, Direction.values()[r], this);
                        if (isPlaceable(m)) {
                            ml.add(m);
                        }
                    }
                }
            }
        }
        return ml;
    }

    private List<Building> getPlaceableBuildings(Building building) {

        if (building == null) {
            return getBuildings().stream().distinct().collect(Collectors.toList());
        } else {
            List<Building> list = new ArrayList<>();
            list.add(building);
            return list;
        }

    }

    @Override
    public String toString() {
        return "Spieler " + name + " (" + color + ")";
    }

    public List<Move> getViableMoves() {
        return viableMoves;
    }

    public String getResult() {
        return name + " (" + color + ") scored " + countPoints() + " Points";
    }

    public List<Building> getBiggestBuilding(List<Building> triedBuildings) {
        int buildingSize  = buildings.stream()
                .filter(building -> !triedBuildings.contains(building))
                .map(Building::getSize)
                .max(Comparator.comparingInt(o -> o))
                .orElse(0);
        return buildings.stream().
                filter(building -> building.getSize() >= (buildingSize == 6 ? buildingSize : buildingSize - 2))
                .toList();
    }
}