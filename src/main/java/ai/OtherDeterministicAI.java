package ai;

import ai.heuristic.Heuristic;
import ai.heuristic.MaximizeDeltaAreasizeHeuristic;
import ai.heuristic.MaximizeDeltaScoreHeuristic;
import ai.heuristic.MoveResult;
import game_logic.Board;
import game_logic.Building;
import game_logic.Move;
import game_logic.Player;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/* Heuristiken:
 *
 * -> Summe der Punkte, die man durch das Setzen der züge erreicht minimieren
 * Maximierung von gebiet eingenommen, wenn möglich.
 *
 */
public class OtherDeterministicAI extends AI {

    private static final Random random = new Random();
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
        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(scoreFactor);
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(areaSizeFactor * 100);


        this.addHeuristic(maximizeScore);
        this.addHeuristic(maximizeAreaSize);
    }


    @Override
    public Move getMove(Board board, Player player) {
        //Wir wollen optimieren für delta anzahlzüge in 3 zügen zukunft.
        Move nextMove;
        List<Building> triedBuildings = new ArrayList<>();

        do {
            List<Building> biggestunused = player.getBiggestBuilding(b -> !triedBuildings.contains(b));
            List<Move> moveList;
            if (biggestunused.size() == 1) {
                if (biggestunused.get(0).getSize() == 6) moveList = player.generateValidMoves(biggestunused);
                else {
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

    //Schauen uns für die n vielversprechendsten züge die "nächsten" m moves von uns an.
    //wobei n,m ausprobiert werden muss.
    //sind die metriken zu stark??

    //Versuche anzahl der unspielbaren punkte des gegners maximieren.

    private Move determineBestMove(List<Move> possibleMoveList, Player player) {
        List<MoveResult> promisingMoves;

        ZonedDateTime start;
        ZonedDateTime end;
        //wir holen 100 moves, von allen moves, alle moves müssen berechnet werden. dauert nicht lange!
        promisingMoves = this.getBestMove(possibleMoveList, player.getGame(), 100);


        List<Future<MoveResult>> tmpValues = null;
        ExecutorService service = Executors.newFixedThreadPool(20);
        List<Callable<MoveResult>> threads = new ArrayList<>();
        List<MoveResult> PromisingNonNullMoves = promisingMoves.stream().filter(Objects::nonNull).toList();
        for (MoveResult m : PromisingNonNullMoves) {
            threads.add(new DeterministicThreading(this, player.getGame(), m));
        }
        try {
            tmpValues = service.invokeAll(threads);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        do {
            assert tmpValues != null;
            if (tmpValues.get(i).isDone()) {
                i++;
            }
        } while (i < tmpValues.size());
        return getBestMoveFromFutures(tmpValues);

    }

    private Move getBestMoveFromFutures(List<Future<MoveResult>> tmpValues) {
        List<MoveResult> results = getResultsFromFuture(tmpValues);
        Optional<MoveResult> bestMoveResult = results.stream().max(MoveResult::compareTo);
        return bestMoveResult.map(MoveResult::getMove).orElse(null);
    }

    private List<MoveResult> getResultsFromFuture(List<Future<MoveResult>> tmpValues) {

        return tmpValues.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    public void printBestNumbers() {
        System.out.println("x2= " + areaSizeFactor + ",x3=" + scoreFactor);
    }
}