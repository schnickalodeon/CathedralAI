package ai.heuristic;

import game_logic.Building;
import game_logic.Move;

public class MaximizeEnemyUseLessPieces extends Heuristic {
    public MaximizeEnemyUseLessPieces(float factor) {
        super(factor);
    }

    @Override
    protected float calculateScore(Move move) {
        testGame.getActivePlayer().makeMove(move);
        int validMoves = testGame.getInactivePlayer().getBuildings()
                .stream()
                .filter(building -> testGame.getInactivePlayer().generateValidMoves(building).size() != 0)
                .mapToInt(Building::getSize)
                .sum();
        int validOpponentsMoves = testGame.getActivePlayer().getBuildings()
                .stream()
                .filter(building -> testGame.getActivePlayer().generateValidMoves(building).size() != 0)
                .mapToInt(Building::getSize)
                .sum();
        return validMoves - validOpponentsMoves;

    }
}
