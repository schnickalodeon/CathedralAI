package ai;

import game_logic.Board;
import game_logic.Move;
import game_logic.Player;
import game_logic.buildings.Cathedral;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CleverRandomAI extends AI {

    private static final Random random = new Random();

    @Override
    public Move getMove(Board board, Player player) {
        List<Move> moveList = player.getViableMoves();
        return moveList.get(random.nextInt(moveList.size()));
    }


    @Override
    public void printBestNumbers() {

    }
}
