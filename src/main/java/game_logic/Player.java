package game_logic;

import ai.AI;
import game_logic.buildings.Cathedral;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player {
    protected final PlayerColor color;
    protected final String name;
    protected final Game game;
    protected final AI ai;
    protected List<Building> buildings = new ArrayList<>();
    protected List<Move> viableMoves = new ArrayList<>();
    protected int timeOuts =0;

    private int bufferInSeconds;

    public PlayerColor getColor() {
        return color;
    }
    public List<Building> getBuildings() { return buildings; }
    private boolean isFirstTurn(){
        return game.getTurnNumber() == 0 && color == PlayerColor.WHITE;
    }

    private Board getBoard(){
        return game.getBoard();
    }

    public boolean hasBuffer() {
        return bufferInSeconds > 0;
    }

    public int getBuffer() {
        return bufferInSeconds;
    }

    protected Player(PlayerColor color, String name, Game game, AI ai) {
        this.color = color;
        this.name = name;
        this.game = game;
        this.ai = ai;
        this.bufferInSeconds = 2; //120;
    }

    public int countPoints(){
        return buildings.stream().mapToInt(b -> b.points.size()).sum();
    }

    public void removeBuildiung(Building building){
        buildings.remove(building);
    }

    public abstract Move getFirstMove() throws Exception;

    public Move getNextMove()
    {
        Building building = (isFirstTurn()) ? new Cathedral(): null;
        viableMoves = generateValidMoves(building);
        if (viableMoves.isEmpty())
        {
            return null;
        }
        return ai.getMove(getBoard(),this);
    }

    public boolean makeMove(Move move)
    {
        if(!isPlaceable(move)){
          return false;
        }

        List<Point> occupiedBoardPositions = move.getOccupyingPoints();
        FieldContent content = move.getBuilding().getContent();
        getBoard().setContent(occupiedBoardPositions,content);
        removeBuildiung(move.getBuilding());
        return true;
    }

    public boolean isPlaceable(Move move){
        List<Point> points = move.getOccupyingPoints();

        for (Point point: points) {
            if(getBoard().isOutOfBounds(point))
            {
                return false;
            }
            FieldContent fieldContent = getBoard().getContent(point);
            if(fieldContent != FieldContent.EMPTY){
                return false;
            }
        }
        return true;
    }

    //TODO Simplify and Refactor
    //for each x,y value, try every building in every rotation, check if its viable if so, add to a list.
    public List<Move> generateValidMoves(Building building)
    {
        List<Move> ml = new ArrayList<>();
        for (int x=0; x<10; x++)
        {
            for(int y=0; y<10; y++)
            {

                //If moves for a specific Building should be generated, use only these.
                List<Building> distinctBuildings = new ArrayList<>();
                if(building == null){
                    distinctBuildings.addAll(buildings.stream().distinct().collect(Collectors.toList()));
                }
                else
                {
                    distinctBuildings.add(building);
                }

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
        return "Spieler " + name + " (" + color + ")";
    }

    public void incrementTimeOut() {
        ++timeOuts;
    }
    public int getTimeOuts(){return timeOuts;}
    public List<Move> getViableMoves(){return viableMoves;}

    public String getResult(){
        return name + " (" + color + ") scored " + countPoints() + " Points";
    }

    public void reduceBuffer(long bufferUsed) {
        bufferInSeconds -= bufferUsed;
    }


}