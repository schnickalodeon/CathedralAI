package ai.heuristic;

import game_logic.Move;

import java.util.List;
import java.util.Optional;

public class FirstMoveHeuristic implements Heuristic {

    @Override
    public Move getBestMove(List<Move> moves) {
        Optional<Move> moveOptional = moves.stream().findFirst();
        return moveOptional.isPresent() ? moveOptional.get() : null;
    }
}
