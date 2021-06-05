package ai;

import ai.ArtificialIntelligent;
import ai.heuristic.Heuristic;
import ai.heuristic.MoveResult;
import game_logic.Game;
import game_logic.Move;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class AI implements ArtificialIntelligent {
    private List<Heuristic> heuristics = new ArrayList<>();

    protected void addHeuristic(Heuristic heuristic){
        heuristics.add(heuristic);
    }

    protected List<MoveResult> getBestMove(List<Move> possibleMoves, Game game, int numResults){
        List<MoveResult> results = new ArrayList<>();
        List<MoveResult> bestMove = new ArrayList<>();

        possibleMoves.forEach(m -> {
            float score = calculateScore(m, game);
            results.add(new MoveResult(m,score));
        });

        results.sort((r1, r2) -> r2.getScore().compareTo(r1.getScore()));

        for (int i=0; i< numResults; i++)
        {
            bestMove.add(results.size() >= i+1? results.get(i):null);
        }
        return bestMove;
    }


    private float calculateScore(Move move, Game game){

        float score = 0;
        for(Heuristic h: heuristics){
            MoveResult result = h.evaluate(move, game);

            score += result.getScore();
        };

        return score;
    }

}
