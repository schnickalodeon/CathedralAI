package game_logic;

import ai.AI;
import game_logic.buildings.Cathedral;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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

    public Board getBoard(){
        return game.getBoard();
    }
    public Game getGame(){return game;}

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
    protected Player(Player player)
    {
        this.color = player.color;
        this.name = player.name;
        this.game = new Game(player.game);
        this.ai = player.ai;
        this.buildings = new ArrayList<>(player.buildings);
        this.viableMoves = new ArrayList<>(player.viableMoves);
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
            if (getBoard().isOutOfBounds(point)) {
                return false;
            }
            FieldContent fieldContent = getBoard().getContent(point);
            if (color == PlayerColor.BLACK) {
                if (fieldContent != FieldContent.EMPTY && fieldContent != FieldContent.BLACK_TERRITORY)
                {
                    return false;
                }
            }
            else
            {
                if (fieldContent != FieldContent.EMPTY && fieldContent != FieldContent.WHITE_TERRITORY)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Move> generateValidMoves(List<Building> buildings)
    {
        ArrayList<Move> ml = new ArrayList<>();
        buildings.forEach(b->
        {
            ml.addAll(generateValidMoves(b));
        });
        return ml;
    }
    //TODO describe every function
    //for each x,y value, try every building in every rotation, check if its viable if so, add to a list.
    public List<Move> generateValidMoves(Building building)
    {
        List<Move> ml = new ArrayList<>();
        for (int x=0; x<10; x++)
        {
            for(int y=0; y<10; y++)
            {
                //If moves for a specific Building should be generated, use only these.
                for(Building b: getPlaceableBuildings(building))
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

    private List<Building> getPlaceableBuildings(Building building) {

        if(building == null){
           return getBuildings().stream().distinct().collect(Collectors.toList());
        }
        else
        {
            List<Building> list = new ArrayList<>();
            list.add(building);
            return list;
        }

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


    public List<Building> getBiggestBuilding(Predicate<Building> filter)
    {
        List<Building>  bigBuildings = new ArrayList<>();
        List<Building> filteredBuildings  = buildings.stream().filter(filter).collect(Collectors.toList());
        int maxSize=0;
        for( Building b: filteredBuildings)
        {
            if (b.getSize() > maxSize)
            {
                bigBuildings.clear();
                bigBuildings.add(b);
                maxSize = b.getSize();
            }
            else if (b.getSize()== maxSize)
            {
                bigBuildings.add(b);
            }
        }
        return bigBuildings;
    }
}