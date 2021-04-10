package ai;

import game_logic.Board;
import game_logic.Move;
import game_logic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CleverRandomAI implements AI{

    private static final Random random = new Random();

    @Override
    public Move getMove(Board board, Player player) {
        List<Move> moveList = player.getMoveList();
        return moveList.get(random.nextInt(moveList.size()));
    }
}
