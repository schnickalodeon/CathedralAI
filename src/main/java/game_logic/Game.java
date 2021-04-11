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
    private final Player white;
    private final Player black;
    private boolean isFinished;
    private int turnNumber = 0;

    private ArrayList<Move> moves = new ArrayList<>();

    public int getTurnNumber(){ return turnNumber; }

    public Board getBoard(){ return board; }

    public Game(AI aiWhite, AI aiBlack) {
        this.board = new Board();
        this.white = new PlayerWhite("Alice", this, aiWhite);
        this.black = new PlayerBlack("Bob", this, aiBlack);
        this.isFinished = false;
    }

    public void start(){
        createGameFile();
        System.out.println("starting game...");

        //First Move


        while(!isFinished){
            turn();
        }

        printResult();

        closeGameFile();
    }

    private void printResult() {
        System.out.println("\nThe Game is over!!\n------------------------------");
        System.out.println(white.getResult());
        System.out.println(black.getResult());
    }

    public void turn(){
        LocalTime startTurn = LocalTime.now();
        Player player = getActivePlayer();
        boolean wasSuccessful = false;
        do {
            if(startTurn.until(LocalTime.now(), ChronoUnit.SECONDS)>=15)
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
                System.out.println(move);
                moves.add(move);
                appendGameFile();
            }

        } while(!wasSuccessful);
        turnNumber++;
        checkGameOver();
    }

    private void checkGameOver() {
        if(white.getViableMoves().isEmpty() && black.getViableMoves().isEmpty()  && !moves.isEmpty())
        {
            isFinished = true;
        }
    }

    private Player getActivePlayer() {
        // White or Black
        return (turnNumber % 2 == 0) ? white : black;
    }

    private void appendGameFile(){
        String boardContent = board.getBoardHtml(turnNumber);
        try(FileWriter writer = new FileWriter(GAME_FILE_NAME,true)) {
            writer.append(boardContent);
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void createGameFile(){

        try(FileWriter writer = new FileWriter(GAME_FILE_NAME,true)) {
            writer.append("<html>");
            writer.append("<body style=\"background-color:grey;\">");
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void closeGameFile(){
        try(FileWriter writer = new FileWriter(GAME_FILE_NAME,true)) {
            writer.append("</body>");
            writer.append("</html>");

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

    }
}