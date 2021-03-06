package ai.heuristic;

import game_logic.Building;
import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.List;

public class MaximizeDeltaPosibleMovesHeuristic extends Heuristic {

    public MaximizeDeltaPosibleMovesHeuristic(float factor) {
        super(factor);
    }

    @Override
    protected float calculateScore(Move move) {

        int ourPossibleNextMoves = calculatePossibleNextMoves(testGame.getActivePlayer());
        int opponentPossibleNextMoves = calculatePossibleNextMoves(testGame.getInactivePlayer());

        return (ourPossibleNextMoves - opponentPossibleNextMoves) * factor*factor*factor;
    }

    private int calculatePossibleNextMoves(Player player){
        int nextPossibleMoves = player.generateValidMoves((Building) null).size();
        return nextPossibleMoves;
    }

}
