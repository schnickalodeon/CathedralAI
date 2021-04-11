package game_logic;

import ai.AI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {
    protected final PlayerColor color;
    protected final String name;
    protected final Board board;
    protected final AI ai;
    protected List<Building> buildings = new ArrayList<>();
    protected List<Move> moveList = new ArrayList<>();
    protected int timeOuts =0;

    public PlayerColor getColor() {
        return color;
    }
    public List<Building> getBuildings() { return buildings; }


    protected Player(PlayerColor color, String name, Board board, AI ai) {
        this.color = color;
        this.name = name;
        this.board = board;
        this.ai = ai;
    }

    public void removeBuildiung(Building building){
        buildings.remove(building);
    }

    public Move getNextMove()
    {
        moveList = generateValidMoves();
        if (moveList.isEmpty())
        {
            return null;
        }
        return ai.getMove(board,this);
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
            if(board.isOutOfBounds(point))
            {
                return false;
            }
            FieldContent fieldContent = board.getContent(point);
            if(fieldContent != FieldContent.EMPTY){
                return false;
            }
        }
        return true;
    }

    //for each x,y value, try every building in every rotation, check if its viable if so, add to a list.
    public List<Move> generateValidMoves()
    {
        List<Move> ml = new ArrayList<>();
        for (int x=0; x<10; x++)
        {
            for(int y=0; y<10; y++)
            {
                List<Building> distinctBuildings = buildings.stream().distinct().collect(Collectors.toList());
                for(Building b: distinctBuildings)
                {
                    for(int r=0; r < b.getTurnable(); r++)
                    {
                        Move m = new Move(new Point(x,y),b, Direction.values()[r],this);
                        if(isPlaceable(m))
                        {
                            ml.add(m);
                        }
                    }
                }
            }
        }
        return ml;
    }

    @Override
    public String toString() {
        return "Spieler " + name + "(" + color + ")";
    }

    public void incrementTimeOut() {
        ++timeOuts;
    }
    public int getTimeOuts(){return timeOuts;}
    public List<Move> getMoveList(){return moveList;}
}