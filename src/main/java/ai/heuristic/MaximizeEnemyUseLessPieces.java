package ai.heuristic;

import game_logic.Building;
import game_logic.Move;

public class MaximizeEnemyUseLessPieces extends Heuristic
{
    public MaximizeEnemyUseLessPieces(float factor)
    {
        super(factor);
    }

    @Override
    protected float calculateScore(Move move) {
        int score=0;
        int sum=0;
        for(Building b : testGame.getInactivePlayer().getBuildings()) {
            int x =testGame.getInactivePlayer().generateValidMoves(b).size();
            sum +=x;
            if ( x < 10) {
                score += (10 - x);
            }
        }
        return score;
    }
}
