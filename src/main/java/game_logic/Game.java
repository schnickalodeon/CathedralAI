package game_logic;

import ai.AI;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Game {
    private static final int TURN_MAX_TIME = 3;
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
        LocalTime bufferStart = null;
        boolean wasSuccessful = false;
        do {

            Move move = player.getNextMove();
            bufferStart = null;
            long timeElapsed = startTurn.until(LocalTime.now(), ChronoUnit.SECONDS);
            if(timeElapsed >= TURN_MAX_TIME)
            {
                System.out.println(player.toString() + ": " + player.getBuffer());
                if(!player.hasBuffer()){
                    System.out.println("Jüm häpt kien tiet mehr");
                    break;
                }
                bufferStart = LocalTime.now();
            }

            if (move == null)
            {
                System.out.println("failed to move!");
                break;
            }
            wasSuccessful = player.makeMove(move);
            if(wasSuccessful){
                System.out.println(move);
                board.checkArea(move);
                if(bufferStart != null){
                    long bufferUsed = bufferStart.until(LocalTime.now(),ChronoUnit.SECONDS);
                    player.reduceBuffer(bufferUsed);
                }

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


        //TODO processing frontend for manual play
    }
}