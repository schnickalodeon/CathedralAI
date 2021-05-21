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

    private static final Random random = new Random();
    public DeterministicAI(float x1_, float x2_)
    {
        x1= x1_;
        x2= x2_;
    }


    @Override
    public Move getMove(Board board, Player player)
    {
        //Wir wollen optimieren für delta anzahlzüge in 3 zügen zukunft.
        Move nextMove;
        List<Building> triedBuildings = new ArrayList<>();

        do {
                List<Building> biggestunused = player.getBiggestBuilding(b->!triedBuildings.contains(b));
                List<Move> moveList = player.generateValidMoves(biggestunused);
                nextMove = determineBestMove(moveList, player);
                if (nextMove == null) triedBuildings.addAll(biggestunused);
                if (biggestunused.isEmpty()) return null;
            }
        while(nextMove == null);

        return nextMove;
    }

    private Move determineBestMove(List<Move> possibleMoveList, Player player)
    {
        Move bestMove = null;
        float highestNumPossibleTurns = -100000;
        for(Move possibleMove : possibleMoveList)
        {
            Game testGame = new Game(player.getGame());
         //figur platzieren
            // figur entfernen
            testGame.getActivePlayer().makeMove(possibleMove);
         //board anschauen
            //Building muss null sein, damit alle aufgerufen werden, ist allerdings ien überladener Constructor,
            // deswegebn muss null getypecasted werden

            int nextPossibleMoves = testGame.getActivePlayer().generateValidMoves((Building) null).size();

            int capturedAreaSize= testGame.getBoard().getCapturedArea(testGame.getActivePlayer().getColor());

            //15
            int possibleMovesOpponent = testGame.getInactivePlayer().generateValidMoves((Building) null).size();

            float diffPossibleMoves = (nextPossibleMoves - possibleMovesOpponent)*x1 +(capturedAreaSize)*x2;
            if(diffPossibleMoves > highestNumPossibleTurns)
            {
                bestMove = possibleMove;
                highestNumPossibleTurns = nextPossibleMoves;
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

    public void printBestNumbers()
    {
        System.out.println("x1= "+x1+ " ,x2= "+x2);
    }
}