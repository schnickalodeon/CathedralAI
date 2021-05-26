package ai.heuristic;

import game_logic.Move;
import java.util.List;

public interface Heuristic {
    Move getBestMove(List<Move> moves);
}
