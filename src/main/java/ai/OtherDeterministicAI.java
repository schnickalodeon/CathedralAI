package ai;

import ai.heuristic.*;
import game_logic.*;
import game_logic.buildings.Cathedral;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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

    float areaSizeFactor;
    float scoreFactor;

    private static final Random random = new Random();

    public OtherDeterministicAI(float x2, float x3) {
        this.areaSizeFactor = x2;
        this.scoreFactor = x3;
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
        ZonedDateTime iterationStart;
        ZonedDateTime iterationEnd;

        int moveSelector = 0;
        float moveScore = 0;
        int pointer = 0;

        //wir holen 100 moves, von allen moves, alle moves müssen berechnet werden. dauert nicht lange!
        promisingMoves = this.getBestMove(possibleMoveList, player.getGame(), 100);


        List<Integer> loopTimes = new ArrayList<>();
        List<Future<MoveResult>> tmpValues = null;
        start = ZonedDateTime.now();
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Callable<MoveResult>> threads = new ArrayList<>();
        for (MoveResult m : promisingMoves.stream().filter(m -> m != null).collect(Collectors.toList())) {
            threads.add(new DeterministicThreading(this, player.getGame(), m));
        }
        try {
            tmpValues = service.invokeAll(threads);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0;
        while (i < 100) {
            if (tmpValues.get(i).isDone()) {
                i++;
            }
        }

        return getBestMoveFromFutures(tmpValues);

        //für jeder dieser 3 variablen berechne ich jetzt den nächsten zug, danach mache Ich den zug, der den
        //höchsten score gibt!




        /*
        MoveResult bestResult = null;
        Heuristic michelsSuperHeuristic = new MichelsSuperHeuristic(player,x1,x2,x3, possibleMoveList);
        //Heurisic terkjkafsj = new MA();
        //


        List<MoveResult> moves = michelsSuperHeuristic.evaluate();

        Optional<MoveResult> first = moves.stream().findFirst();
        bestResult = first.isPresent() ? first.get() : null;

        return bestResult.getMove();

         */
    }

    private Move getBestMoveFromFutures(List<Future<MoveResult>> tmpValues) {
        tmpValues.stream().max((m1,m2) -> {
            try {
                return Float.compare(m1.get().getScore(),m2.get().getScore());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).get().get().getMove();
    }

    public void printBestNumbers() {
        System.out.println("x2= " + areaSizeFactor + ",x3=" + scoreFactor);
    }
}