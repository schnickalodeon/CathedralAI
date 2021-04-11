package ai;

import game_logic.Board;
import game_logic.Move;
import game_logic.Player;
import game_logic.buildings.Cathedral;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CleverRandomAI implements AI{

    private static final Random random = new Random();

    @Override
    public Move getMove(Board board, Player player) {
        List<Move> moveList = player.getViableMoves();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return moveList.get(random.nextInt(moveList.size()));
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
