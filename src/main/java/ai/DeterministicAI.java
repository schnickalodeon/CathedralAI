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


    private static final Random random = new Random();


    @Override
    public Move getMove(Board board, Player player)
    {
        //Wir wollen optimieren für delta anzahlzüge in 3 zügen zukunft.
        Move nextMove = null;
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
        int highestNumPossibleTurns = -100000;
        for(Move possibleMove : possibleMoveList)
        {
            Game testGame = new Game(player.getGame());
         //figur platzieren
            // figur entfernen
            testGame.getActivePlayer().makeMove(possibleMove);
         //board anschauen
            //Building muss null sein, damit alle aufgerufen werden, ist allerdings ien überladener Constructor,
            // deswegebn muss null getypecasted werden

            //20
            int nextPossibleMoves = testGame.getActivePlayer().generateValidMoves((Building) null).size();
            int possibleSum = 0;
            int possibleSumOpponent =0;
            for(Building b : testGame.getActivePlayer().getBuildings())
            {
                possibleSum += b.getSize();
            }
            for(Building b : testGame.getInactivePlayer().getBuildings())
            {
                possibleSumOpponent += b.getSize();
            }
            //15
            int possibleMovesOpponent = testGame.getInactivePlayer().generateValidMoves((Building) null).size();

            int diffPossibleMoves = nextPossibleMoves - possibleMovesOpponent +(possibleSumOpponent-possibleSum)*150;
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
}