package ai.heuristic;

import game_logic.Building;
import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.List;

public class MichelsSuperHeuristic extends Heuristic {

    private Player player;

    //Faktoren
    private float x1;
    private float x2;
    private float x3;

    public MichelsSuperHeuristic(Player player, float x1, float x2, float x3, List<Move> posibleMoves) {
        this.player = player;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.moves = posibleMoves;
    }

    public MoveResult getBestMove() {
        Move bestMove = null;

        float highestNumPossibleTurns = -Float.MAX_VALUE;
        for (Move possibleMove : moves) {
            Game testGame = new Game(player.getGame());
            //figur platzieren
            // figur entfernen
            testGame.getActivePlayer().makeMove(possibleMove);
            //board anschauen
            //Building muss null sein, damit alle aufgerufen werden, ist allerdings ien überladener Constructor,
            // deswegebn muss null getypecasted werden


            //vars for testing
            int nextPossibleMoves = testGame.getActivePlayer().generateValidMoves((Building) null).size();
            int capturedAreaSize = testGame.getBoard().getCapturedArea(testGame.getActivePlayer().getColor());
            int capturedAreaSizeOpponent = testGame.getBoard().getCapturedArea(testGame.getInactivePlayer().getColor());
            int possibleMovesOpponent = testGame.getInactivePlayer().generateValidMoves((Building) null).size();
            int currentScore=0;

            for(Building  b: testGame.getActivePlayer().getBuildings())
            {
                currentScore += b.getSize();
            }
            int currentScoreOpponent=0;
            for (Building b: testGame.getInactivePlayer().getBuildings())
            {
                currentScoreOpponent += b.getSize();
            }

            //(nextPossibleMoves - possibleMovesOpponent) * x1
            float possibleMovesFactored =(nextPossibleMoves - possibleMovesOpponent) ;
            float deltaAreaSize= (capturedAreaSize-capturedAreaSizeOpponent);
            float deltaCurrentScore = (currentScoreOpponent-(currentScore-possibleMove.getBuilding().getSize()));
            float diffPossibleMoves = possibleMovesFactored*x1 + deltaAreaSize*x2*x2 +deltaCurrentScore*x3;
            if (diffPossibleMoves > highestNumPossibleTurns) {
                bestMove = possibleMove;
                highestNumPossibleTurns = diffPossibleMoves;
            }
        }
        return new MoveResult(bestMove,0);
    }

    @Override
    protected float calculateScore() {
        return 0;
    }
}
