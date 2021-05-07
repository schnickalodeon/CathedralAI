package ai;

import game_logic.Board;
import game_logic.Building;
import game_logic.Move;
import game_logic.Player;
import game_logic.buildings.buildingSizeComparitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DeterministicAI implements AI
{

    private static final Random random = new Random();


    @Override
    public Move getMove(Board board, Player player)
    {
        //Wir wollen optimieren für delta anzahlzüge in 3 zügen zukunft.
        //nur das mit den Meisten points in betracht ziehen.
        //implementieren das wir uns vorstellen können wie das Board aussieht nach diesem zug.
        //optimieren nach maximum zügen nach diesem zug.
        // --> optimieren nach maximum am ende des Nächsten gegnerzuges bei "perfektem spiel"
        // -> nach n zügen.

        List<Move> moveList = getMovesWithBiggestBuildings(player);
        int tmp = random.nextInt(moveList.size());
        return moveList.get(tmp);
    }

    private List<Move> getMovesWithBiggestBuildings(Player player)
    {
        List<Building> biggestBuildings = player.getBiggestBuilding();
        List<Move> moveList = player.generateValidMoves(biggestBuildings);


        return moveList;

    }

    @Override
    public Move getFirstMove(Board board, Player player) {

        return null;
    }
}
