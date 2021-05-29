package ai.heuristic;

import game_logic.Move;

import java.util.List;

public interface Heuristicable {
    List<MoveResult> evaluate();
}
