package ai.heuristic;

import game_logic.Game;
import game_logic.Move;

import java.util.List;

public interface Heuristicable {
    List<MoveResult> evaluate(List<Move> moves, Game game);
    MoveResult evaluate(Move move, Game game);
}
