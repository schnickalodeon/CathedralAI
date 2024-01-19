package ai;

import ai.heuristic.*;
import game_logic.Board;
import game_logic.Building;
import game_logic.Move;
import game_logic.Player;

import java.util.*;
import java.util.concurrent.*;

/* Heuristiken:
 *
 * -> Summe der Punkte, die man durch das Setzen der züge erreicht minimieren
 * Maximierung von gebiet eingenommen, wenn möglich.
 *
 */
public class OtherDeterministicAI extends AI {

    private final float areaSizeFactor;
    private final float scoreFactor;

    public OtherDeterministicAI(float x2, float x3) {
        this.areaSizeFactor = x2;
        this.scoreFactor = x3;
        addHeuristics();
    }

    public OtherDeterministicAI(OtherDeterministicAI ai) {
        this.areaSizeFactor = ai.getAreaSizeFactor();
        this.scoreFactor = ai.getScoreFactor();
        addHeuristics();
    }

    public float getAreaSizeFactor() {
        return areaSizeFactor;
    }

    public float getScoreFactor() {
        return scoreFactor;
    }

    private void addHeuristics() {
        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(scoreFactor );
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(areaSizeFactor*5);
        Heuristic usablePieces = new MaximizeEnemyUseLessPieces(20);

        this.addHeuristic(maximizeScore);
        this.addHeuristic(maximizeAreaSize);
        this.addHeuristic(usablePieces);
    }

    @Override
    public Move getMove(Board board, Player player) {
        Move nextMove;
        List<Building> triedBuildings = new ArrayList<>();
        do {
            List<Building> biggestUnused = player.getBiggestBuilding(triedBuildings);
            List<Move> moveList = player.generateValidMoves(biggestUnused);
            if (biggestUnused.size() != 0 && biggestUnused.get(0).getSize() == 6) {
                Random r = new Random();
                return moveList.get(r.nextInt(moveList.size()));
            }
            nextMove = determineBestMove(moveList, player);
            if (nextMove == null) triedBuildings.addAll(biggestUnused);
            if (biggestUnused.isEmpty()) return null;
        }
        while (nextMove == null);
        System.out.println(nextMove);
        return nextMove;
    }

    private Move determineBestMove(List<Move> possibleMoveList, Player player) {
        List<MoveResult> promisingMoves;
        promisingMoves = this.getBestMove(possibleMoveList, player.getGame(), 5);
        List<Future<MoveResult>> tmpValues = null;

        ExecutorService service = Executors.newFixedThreadPool(20);
        List<Callable<MoveResult>> threads = new ArrayList<>();

        List<MoveResult> PromisingNonNullMoves = promisingMoves.stream().filter(Objects::nonNull).toList();
        for (MoveResult m : PromisingNonNullMoves) {
            threads.add(new DeterministicThreading(this, player.getGame(), m));
        }
        int i = 0;

        try {
            tmpValues = service.invokeAll(threads);
            do {
                if (i < tmpValues.size() && tmpValues.get(i).isDone()) {
                    i++;
                }
            } while (i < tmpValues.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getBestMoveFromFutures(tmpValues);
    }

    private Move getBestMoveFromFutures(List<Future<MoveResult>> tmpValues) {
        List<MoveResult> results = getResultsFromFuture(tmpValues);
        Optional<MoveResult> bestMoveResult = results.stream().max(MoveResult::compareTo);
        return bestMoveResult.map(MoveResult::getMove).orElse(null);
    }

    private List<MoveResult> getResultsFromFuture(List<Future<MoveResult>> tmpValues) {

        return tmpValues.stream().filter(Objects::nonNull).map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return null;
            }
        }).toList();
    }

    public void printBestNumbers() {
        System.out.println("x2= " + areaSizeFactor + ",x3=" + scoreFactor);
    }

    public String toString() {
        return "Area Factor: " + this.areaSizeFactor + ", Score Factor: " + this.scoreFactor;
    }
}