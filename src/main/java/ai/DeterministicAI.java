package ai;

import ai.heuristic.*;
import game_logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Heuristiken:
 *
 * -> Summe der Punkte, die man durch das Setzen der züge erreicht minimieren
 * Maximierung von gebiet eingenommen, wenn möglich.
 *
 */
public class DeterministicAI extends AI {


    private static final Random random = new Random();

    public DeterministicAI(float possibleMoves, float areaSizeFactor, float scoreFactor) {

        addHeuristics(possibleMoves, areaSizeFactor, scoreFactor);
    }

    private void addHeuristics(float possibleMoves, float areaSize, float score) {
        Heuristic maximizePossibleMoves = new MaximizeDeltaPosibleMovesHeuristic(possibleMoves);
        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(score);
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(areaSize);

        this.addHeuristic(maximizeScore);
        this.addHeuristic(maximizePossibleMoves);
        this.addHeuristic(maximizeAreaSize);
    }


    @Override
    public Move getMove(Board board, Player player) {
        Move nextMove;
        List<Building> triedBuildings = new ArrayList<>();
        do {
            List<Building> biggestunused = player.getBiggestBuilding(triedBuildings);
            List<Move> moveList;
            if (biggestunused.size() == 1) {
                if (biggestunused.get(0).getSize() == 6) {
                    moveList = player.generateValidMoves(biggestunused);
                } else {
                    moveList = player.generateValidMoves(player.getBuildings());
                }
            } else {
                moveList = player.generateValidMoves(player.getBuildings());
            }
            nextMove = determineBestMove(moveList, player);
            if (nextMove == null) triedBuildings.addAll(biggestunused);
            if (biggestunused.isEmpty()) return null;
        }
        while (nextMove == null);

        return nextMove;
    }


    private Move determineBestMove(List<Move> possibleMoveList, Player player) {

        List<MoveResult> PromisingMoves;
        PromisingMoves = this.getBestMove(possibleMoveList, player.getGame(), 100);

        List<Float> allTheGoodMoves = new ArrayList<>();
        int bestMovePointer = 0;
        float moveScore = 0;
        int pointer = 0;

        for (MoveResult moveResult : PromisingMoves) {
            if (moveResult != null) {
                Game test = new Game(player.getGame());
                test.getActivePlayer().makeMove(moveResult.getMove());
                test.getActivePlayer().removeBuildiung(moveResult.getMove().getBuilding());
                test.getBoard().checkBoard(test.getActivePlayer().getColor());
                List<MoveResult> listOfGoodMoves;

                //get 3 good moves.
                listOfGoodMoves = this.getBestMove(test.getActivePlayer().generateValidMoves(test.getActivePlayer().getBuildings()), test, 3);


                float sum = 0;
                int notNullCounter = 0;
                for (MoveResult goodMoves : listOfGoodMoves) {
                    if (goodMoves != null)
                        sum += goodMoves.getScore();
                    notNullCounter++;
                }

                //sometimes there is less than 3 valid moves.
                sum /= notNullCounter;

                if (moveScore < sum) {
                    moveScore = sum;
                    bestMovePointer = pointer;
                }
                pointer++;
            }
        }

        return PromisingMoves.get(bestMovePointer).getMove();

    }

    public void printBestNumbers() {
        System.out.println("factors not implemented, rethink design.");
    }
}