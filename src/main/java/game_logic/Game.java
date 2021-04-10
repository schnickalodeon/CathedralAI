package game_logic;

import ai.AI;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Game {
    private static final String GAME_FILE_NAME = "Game.html";
    private final Board board;
    private final Player cathedral;
    private final Player white;
    private final Player black;
    private boolean isFinished;
    private AI ai;
    private int moveCount;
    private LocalTime startTurn;
    private LocalTime endTurn;

    private ArrayList<Move> moves = new ArrayList<>();

    public Game(AI aiWhite, AI aiBlack) {
        this.board = new Board();
        this.moveCount = 0;
        this.cathedral = new Player(PlayerColor.NEUTRAL,"Cathedral", board, aiBlack);
        this.white = new Player(PlayerColor.WHITE,"Alice", board, aiWhite);
        this.black = new Player(PlayerColor.BLACK,"Bob", board, aiBlack);
        this.isFinished = false;
    }

    public void Start(){
        createGameFile();
        System.out.println("starting game...");
        while(!isFinished){
            Step();
        }
        closeGameFile();
    }

    public void Step(){
        System.out.println("starting step...");
        startTurn = LocalTime.now();
        Player player = getActivePlayer();
        boolean wasSuccessful = false;
        do {
            endTurn = LocalTime.now();
            if(startTurn.until(endTurn, ChronoUnit.SECONDS)>=15)
            {
                //TODO Implement buffer!
                break;
            }
            Move move = player.getNextMove();
            if (move == null)
            {
                System.out.println("failed to move!");
                break;
            }
            wasSuccessful = player.makeMove(move);
            if(wasSuccessful){
                System.out.println("successful turn");
                moves.add(move);
                appendGameFile();
            }

        } while(!wasSuccessful);
        checkGameOver();
    }

    private void checkGameOver() {
        if(white.getMoveList().size() == 0 && black.getMoveList().size() == 0 && moves.size() > 1)
        {
            System.out.println("game is over");
            isFinished = true;
        }
    }

    private Player getActivePlayer() {
        //First Move --> Cathedral
        if(moves.isEmpty())
            return cathedral;

        // White or Black
        return (moves.size() % 2 == 0) ? white : black;
    }

    private void appendGameFile(){
        String boardContent = board.getBoardHtml();
        try {
            FileWriter writer = new FileWriter(GAME_FILE_NAME,true);
            writer.append(boardContent);
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void createGameFile(){
        try {
            FileWriter writer = new FileWriter(GAME_FILE_NAME,true);
            writer.append("<html>");
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void closeGameFile(){
        try {
            FileWriter writer = new FileWriter(GAME_FILE_NAME,true);
            writer.append("</html>");
            writer.close();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}