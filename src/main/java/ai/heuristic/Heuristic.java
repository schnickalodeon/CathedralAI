package ai.heuristic;

import game_logic.Game;
import game_logic.Move;
import game_logic.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Heuristic implements Heuristicable {
    protected float factor;
    private Game game;
    protected Game testGame;
    protected List<Move> moves;


    protected Heuristic(float factor, Game game, List<Move> moves) {
        this.factor = factor;
        this.moves = moves;
        this.game = game;
    }

    public List<MoveResult> evaluate(){
        List<MoveResult> results = new ArrayList<>();

        for(Move m: moves){
            testGame = new Game(game);
            testGame.getActivePlayer().makeMove(m);
            float score =  calculateScore();
            MoveResult result = new MoveResult(m,score);
            results.add(result);
        }
        return results;
    }



    protected abstract float calculateScore();
}
