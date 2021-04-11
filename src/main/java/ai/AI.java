package ai;

import game_logic.Board;
import game_logic.Move;
import game_logic.Player;

public interface AI {
    Move getMove(Board board, Player player);
}
