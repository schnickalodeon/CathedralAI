package ai;

import ai.heuristic.Heuristic;
import ai.heuristic.MichelsSuperHeuristic;
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
public class DeterministicAI implements AI
{
    float x1;
    float x2;
    float x3;

    private static final Random random = new Random();

    public DeterministicAI(float x1_, float x2_, float x3) {
        x1 = x1_;
        x2 = x2_;
        this.x3= x3;
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
        Move bestMove = null;
        Heuristic michelsSuperHeuristic = new MichelsSuperHeuristic(player,x1,x2,x3);

        bestMove = michelsSuperHeuristic.getBestMove(possibleMoveList);

        return bestMove;
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
        System.out.println("x1= " + x1 + " ,x2= " + x2+ ",x3="+x3);
    }
}