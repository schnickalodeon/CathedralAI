package ai;

import game_logic.Board;
import game_logic.Move;
import game_logic.Player;

public interface ArtificialIntelligent {
    Move getMove(Board board, Player player);
    Move getFirstMove(Board board, Player player);
    void printBestNumbers();
}
