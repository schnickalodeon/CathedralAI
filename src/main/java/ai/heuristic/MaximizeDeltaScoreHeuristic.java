package ai.heuristic;

import game_logic.Building;
import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.List;

public class MaximizeDeltaScoreHeuristic extends Heuristic {

    public MaximizeDeltaScoreHeuristic(float factor) {
        super(factor);
    }

    @Override
    protected float calculateScore(Move move) {
       final int ourScore = getScore(testGame.getActivePlayer());
        final int opponentScore = getScore(testGame.getInactivePlayer());

        return (opponentScore - (ourScore-move.getBuilding().getSize())) * factor;
    }

    private int getScore(Player player){
        return player.getBuildings().stream().mapToInt(Building::getSize).sum();
    }
}
