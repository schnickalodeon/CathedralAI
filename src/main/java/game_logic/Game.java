package game_logic;

import GUI.GUI;
import ai.ArtificialIntelligent;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Game {
    private static final boolean DEBUG = false;
    private static final int TURN_MAX_TIME = 3;
    private static final String GAME_FILE_NAME = "Game.html";
    private final Board board;
    private final Player white;
    private final Player black;
    private boolean isFinished;
    private int turnNumber = 0;

    private ZonedDateTime start;
    private ZonedDateTime end;

    private final ArrayList<Move> moves = new ArrayList<>();
    private GUI gui;

    public int getTurnNumber() {
        return turnNumber;
    }

    public ArrayList<Move> getPreviousMoves() {
        return moves;
    }

    public Board getBoard() {
        return board;
    }


    public Game(ArtificialIntelligent artificialIntelligentWhite, ArtificialIntelligent artificialIntelligentBlack, GUI gui) {
        this.board = new Board(this);
        this.white = new PlayerWhite("Alice", this, artificialIntelligentWhite);
        this.black = new PlayerBlack("Bob", this, artificialIntelligentBlack);
        this.isFinished = false;
        while (this.gui == null) {
            this.gui = gui;
        }
        gui.setGame(this);
    }

    public Game(Game game) {
        this.board = new Board(game.board, this);
        this.white = new PlayerWhite(game.white, this);
        this.black = new PlayerBlack(game.black, this);
        this.isFinished = false;
        this.turnNumber = game.turnNumber;
        this.start = game.start;
        this.end =game.end;
        this.gui = game.gui;
    }

    public boolean start() {
        createGameFile();
        start = ZonedDateTime.now();

        while (!isFinished) {
            turn();
        }
        end = ZonedDateTime.now();

        printResult();

        closeGameFile();
        return white.countPoints() > black.countPoints();
    }

    private void printResult() {
        long seconds = ChronoUnit.SECONDS.between(start, end);
        System.out.printf("\nThe Game is over and took %d Seconds\n------------------------------%n", seconds);
        System.out.println(white.getResult());
        System.out.println(black.getResult());
        System.out.println("=========================\n\n");
    }

    public void turn() {
        //LocalTime startTurn = LocalTime.now();
        Player player = getActivePlayer();
        //LocalTime bufferStart;
        boolean wasSuccessful;
        do {

            Move move = player.getNextMove();
/*          bufferStart = null;
            long timeElapsed = startTurn.until(LocalTime.now(), ChronoUnit.SECONDS);
            if(timeElapsed >= TURN_MAX_TIME)
            {
                System.out.println(player.toString() + ": " + player.getBuffer());
                if(!player.hasBuffer()){
                    System.out.println("Jüm häpt kien tiet mehr");
                    break;
                }
                bufferStart = LocalTime.now();
            }*/

            if (move == null) {
                if (DEBUG) System.out.println("failed to move!");
                break;
            }
            wasSuccessful = player.makeMove(move);
            if (wasSuccessful) {
                if (DEBUG) {
                    System.out.println(move);
                }

                board.checkArea(move.getPlayer().getColor());
                /*if(bufferStart != null){
                    long bufferUsed = bufferStart.until(LocalTime.now(),ChronoUnit.SECONDS);
                    player.reduceBuffer(bufferUsed);
                }*/

                moves.add(move);
                appendGameFile();
            }
        } while (!wasSuccessful);
        turnNumber++;
        checkGameOver();


    }

    private void checkGameOver() {
        if (white.getViableMoves().isEmpty() && black.getViableMoves().isEmpty() && !moves.isEmpty()) {
            isFinished = true;
        }
    }

    public Player getActivePlayer() {
        // White or Black
        return (turnNumber % 2 == 0) ? white : black;
    }

    public Player getInactivePlayer() {
        return (turnNumber % 2 == 0) ? black : white;
    }

    private void appendGameFile() {
        String boardContent = board.getBoardHtml(turnNumber);
        try (FileWriter writer = new FileWriter(GAME_FILE_NAME, true)) {
            writer.append(boardContent);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createGameFile() {

        try (FileWriter writer = new FileWriter(GAME_FILE_NAME, true)) {
            writer.append("<html>");
            writer.append("<body style=\"background-color:grey;\">");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void closeGameFile() {
        try (FileWriter writer = new FileWriter(GAME_FILE_NAME, true)) {
            writer.append("</body>");
            writer.append("</html>");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }
}