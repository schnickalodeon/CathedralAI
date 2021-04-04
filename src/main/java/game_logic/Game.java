package game_logic;

import ai.AI;
import game_logic.buildings.Player;

import java.util.ArrayList;

public class Game {
    private final Board board;
    private final Player cathedral;
    private final Player white;
    private final Player black;
    private boolean isFinished;
    private AI ai;
    private ArrayList<Move> moves;

    public Game() {
        this.board = new Board();

        cathedral = new Player(PlayerColor.NEUTRAL,"Cathedral");
        white = new Player(PlayerColor.WHITE,"Alice");
        black = new Player(PlayerColor.BLACK,"Bob");

        isFinished = false;
    }

    public void Start(){

        while(!isFinished){
            Step();
        }
    }

    public void Step(){
        Player player = getActivePlayer();
        Move move = ai.getMove(board,player);

        boolean wasSuccessful = board.place(move);

    }

    private Player getActivePlayer() {
        //First Move --> Cathedral
        if(moves.isEmpty())
            return cathedral;

        // White or Black
        return (moves.stream().count() % 2 == 0) ? white : black;
    }

}
