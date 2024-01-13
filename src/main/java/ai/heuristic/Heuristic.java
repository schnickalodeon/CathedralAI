package ai.heuristic;

import game_logic.Game;
import game_logic.Move;

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
            testGame.getBoard().checkBoard(m.getPlayer().getColor());
            float score =  calculateScore(m);
            MoveResult result = new MoveResult(m,score);
            results.add(result);
        }
        return results;
    }

    public MoveResult evaluate(Move move, Game game){
        testGame = new Game(game);
        testGame.getActivePlayer().makeMove(move);
        testGame.getBoard().checkBoard(move.getPlayer().getColor());
        float score =  calculateScore(move);
        return new MoveResult(move,score);
    }



    protected abstract float calculateScore(Move move);
}
