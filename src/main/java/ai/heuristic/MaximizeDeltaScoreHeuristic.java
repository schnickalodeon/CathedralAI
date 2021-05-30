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
        int ourScore = getScore(testGame.getActivePlayer());
        int opponentScore = getScore(testGame.getInactivePlayer());

        return (opponentScore - (ourScore-move.getBuilding().getSize())) * factor * factor;
    }

    private int getScore(Player player){
        int score = player.getBuildings().stream().mapToInt(Building::getSize).sum();
        return score;
    }
}
