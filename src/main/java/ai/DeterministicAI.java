package ai;

import ai.heuristic.*;
import game_logic.*;
import game_logic.buildings.Cathedral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
/* Heuristiken:
 *
 * -> Summe der Punkte, die man durch das Setzen der züge erreicht minimieren
 * Maximierung von gebiet eingenommen, wenn möglich.
 *
*/
public class DeterministicAI extends AI
{
    float possibleMovesFactor;
    float areaSizeFactor;
    float scoreFactor;

    private static final Random random = new Random();

    public DeterministicAI(float x1_, float x2_, float x3) {
        possibleMovesFactor = x1_;
        areaSizeFactor = x2_;
        this.scoreFactor = x3;
    }


    @Override
    public Move getMove(Board board, Player player)
    {
        //Wir wollen optimieren für delta anzahlzüge in 3 zügen zukunft.
        Move nextMove;
        List<Building> triedBuildings = new ArrayList<>();

        do {
            List<Building> biggestunused = player.getBiggestBuilding(b -> !triedBuildings.contains(b));
            List<Move> moveList;
            if (biggestunused.size() == 1) {
                if(biggestunused.get(0).getSize()==6) moveList = player.generateValidMoves(biggestunused);
                else{moveList = player.generateValidMoves(player.getBuildings());}
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
        Game game = player.getGame();

        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(scoreFactor,game,possibleMoveList);
        Heuristic maximizePossibleMoves = new MaximizeDeltaPosibleMovesHeuristic(scoreFactor,game,possibleMoveList);
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(scoreFactor,game,possibleMoveList);

        this.addHeuristic(maximizeScore);
        this.addHeuristic(maximizePossibleMoves);
        this.addHeuristic(maximizeAreaSize);

        return this.getBestMove(possibleMoveList);

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

    @Override
    public Move getFirstMove(Board board, Player player) {
        Cathedral cathedral = new Cathedral();
        List<Move> moveList = player.getViableMoves()
                .stream()
                .filter(m -> m.getBuilding().equals(cathedral))
                .collect(Collectors.toList());
        return moveList.get(random.nextInt(moveList.size()));
    }

    public void printBestNumbers() {
        System.out.println("x1= " + possibleMovesFactor + " ,x2= " + areaSizeFactor + ",x3="+ scoreFactor);
    }
}