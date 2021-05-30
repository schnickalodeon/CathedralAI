package ai.heuristic;

import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Heuristic implements Heuristicable {
    protected float factor;
    protected Game testGame;


    protected Heuristic(float factor) {
        this.factor = factor;
    }

    public List<MoveResult> evaluate(List<Move> moves, Game game){
        List<MoveResult> results = new ArrayList<>();

        for(Move m: moves){
            testGame = new Game(game);
            testGame.getActivePlayer().makeMove(m);
            testGame.getBoard().checkArea(testGame.getActivePlayer().getColor());
            float score =  calculateScore(m);
            MoveResult result = new MoveResult(m,score);
            results.add(result);
        }
        return results;
    }

    public MoveResult evaluate(Move move, Game game){
        testGame = new Game(game);
        testGame.getActivePlayer().makeMove(move);
        float score =  calculateScore(move);
        return new MoveResult(move,score);
    }



    protected abstract float calculateScore(Move move);
}
