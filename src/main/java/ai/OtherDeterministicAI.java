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
public class OtherDeterministicAI extends AI
{

    float areaSizeFactor;
    float scoreFactor;

    private static final Random random = new Random();

    public OtherDeterministicAI( float x2, float x3) {
        this.areaSizeFactor = x2;
        this.scoreFactor = x3;
        addHeuristics();
    }

    private void addHeuristics(){
        Heuristic maximizeScore = new MaximizeDeltaScoreHeuristic(scoreFactor);
        Heuristic maximizeAreaSize = new MaximizeDeltaAreasizeHeuristic(areaSizeFactor);


        this.addHeuristic(maximizeScore);
        this.addHeuristic(maximizeAreaSize);
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
        List<MoveResult> PromisingMoves;
        PromisingMoves = this.getBestMove(possibleMoveList, player.getGame(),(possibleMoveList.size()/3)>100?100:possibleMoveList.size()/3);


        List<Float> allTheGoodMoves = new ArrayList<>();
        int moveSelector=0;
        float moveScore = 0;
        int pointer=0;
        for (MoveResult m : PromisingMoves) {
            if (m != null) {
                Game test = new Game(player.getGame());
                test.getActivePlayer().makeMove(m.getMove());
                test.getActivePlayer().removeBuildiung(m.getMove().getBuilding());
                List<MoveResult> listOfGoodMoves;
                listOfGoodMoves = this.getBestMove(test.getActivePlayer().generateValidMoves(test.getActivePlayer().getBuildings()), test,3);
                float sum = 0;
                int noNullCounter =0;
                for (MoveResult mr : listOfGoodMoves) {
                    if (mr != null)
                        sum += mr.getScore();
                    noNullCounter ++;
                }
                sum /= noNullCounter;
                if (moveScore < sum) {
                    moveScore = sum;
                    moveSelector = pointer;
                }
                pointer++;
            }
        }

        //für jeder dieser 3 variablen berechne ich jetzt den nächsten zug, danach mache Ich den zug, der den
        //höchsten score gibt!



        return PromisingMoves.get(moveSelector).getMove();

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

    public void printBestNumbers() {
        System.out.println("x2= " + areaSizeFactor + ",x3="+ scoreFactor);
    }
}