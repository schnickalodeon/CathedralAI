
package ai;

import ai.heuristic.*;
import game_logic.*;
import game_logic.buildings.Cathedral;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/* Heuristiken:
 *
 * -> Summe der Punkte, die man durch das Setzen der züge erreicht minimieren
 * Maximierung von gebiet eingenommen, wenn möglich.
 *
 */
public class OtherDeterministicAI extends AI {

    public float getAreaSizeFactor() {
        return areaSizeFactor;
    }

   private float areaSizeFactor;

    public float getScoreFactor() {
        return scoreFactor;
    }

    private float scoreFactor;

    private static final Random random = new Random();

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

    private void addHeuristics() {
        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(scoreFactor);
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(areaSizeFactor);


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
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Callable<MoveResult>> threads = new ArrayList<>();
        List<MoveResult>PromisingNonNullMoves = promisingMoves.stream().filter(m -> m != null).collect(Collectors.toList());
        for (MoveResult m : PromisingNonNullMoves) {
            threads.add(new DeterministicThreading(this, player.getGame(), m));
        }
        try {
            tmpValues = service.invokeAll(threads);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        while (i < promisingMoves.size()) {
            if (tmpValues.get(i).isDone()) {
                i++;
            }
        }
        return getBestMoveFromFutures(tmpValues);

    }

    private Move getBestMoveFromFutures(List<Future<MoveResult>> tmpValues) {
        List<MoveResult> results = getResultsFromFuture(tmpValues);
        Optional<MoveResult> bestMoveResult = results.stream().max(MoveResult::compareTo);
        return bestMoveResult.map(MoveResult::getMove).orElse(null);
    }

    private List<MoveResult> getResultsFromFuture(List<Future<MoveResult>> tmpValues){
        List<MoveResult> result = tmpValues.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        return result;
    }

    public void printBestNumbers() {
        System.out.println("x2= " + areaSizeFactor + ",x3=" + scoreFactor);
    }
}