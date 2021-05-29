package ai.heuristic;

import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.List;

public class MaximizeDeltaAreasizeHeuristic extends Heuristic {

    public MaximizeDeltaAreasizeHeuristic(float factor, Game game, List<Move> moves) {
        super(factor, game, moves);
    }

    @Override
    protected float calculateScore() {
        int ourCapturedAreaSize = calculateCapturedArea(testGame.getActivePlayer());
        int opponentCapturedAreaSize = calculateCapturedArea(testGame.getInactivePlayer());

        return (ourCapturedAreaSize - opponentCapturedAreaSize) * factor;
    }

    private int calculateCapturedArea(Player player){
        int capturedAreaSize = testGame.getBoard().getCapturedArea(player.getColor());
        return capturedAreaSize;
    }
}
