package game_logic;

import java.awt.*;
import java.util.List;

public class Player {
    private final PlayerColor color;
    private final String name;
    private final List<Building> buildings;
    private final Board board;

    public PlayerColor getColor() {
        return color;
    }
    public List<Building> getBuildings() { return buildings; }

    public Player(PlayerColor color, String name, Board board) {
        this.color = color;
        this.name = name;
        this.board = board;

        buildings = Building.getAllBuildingsForPlayer(this);
    }

    public void removeBuildiung(Building building){
        buildings.remove(building);
    }

    public boolean makeMove(Move move)
    {
        if(!isPlaceable(move)){
          return false;
        }

        List<Point> occupiedBoardPositions = move.getOccupyingPoints();
        board.setContent(occupiedBoardPositions,this);
        removeBuildiung(move.getBuilding());
        return true;
    }

    public boolean isPlaceable(Move move){
        List<Point> points = move.getOccupyingPoints();

        for (Point point: points) {
            FieldContent fieldContent = board.getContent(point);
            if(fieldContent != FieldContent.EMPTY){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Spieler " + name + "(" + color + ")";
    }
}
