package ai;

import game_logic.*;
import game_logic.buildings.Cathedral;
import game_logic.buildings.buildingSizeComparitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        float highestNumPossibleTurns = -Float.MAX_VALUE;
        for (Move possibleMove : possibleMoveList) {
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