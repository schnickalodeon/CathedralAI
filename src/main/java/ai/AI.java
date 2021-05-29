package ai;

import ai.ArtificialIntelligent;
import ai.heuristic.Heuristic;
import ai.heuristic.MoveResult;
import game_logic.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class AI implements ArtificialIntelligent {
    private List<Heuristic> heuristics;

    protected void addHeuristic(Heuristic heuristic){
        heuristics.add(heuristic);
    }

    protected Move getBestMove(List<Move> possibleMoves){
        List<MoveResult> results = new ArrayList<>();
        Move bestMove = null;

        possibleMoves.forEach(m -> {
            float score = calculateScore(m);
            results.add(new MoveResult(m,score));
            results.add(new MoveResult(m,score));
        });

        results.sort((r1, r2) -> r2.getScore().compareTo(r1.getScore()));

        bestMove = !results.isEmpty() ? results.get(0).getMove() : null;

        return bestMove;
    }



    private float calculateScore(Move move){

        float score = 0;
        for(Heuristic h: heuristics){
            List<MoveResult> results = h.evaluate();
            MoveResult result = results.stream()
                    .filter(r -> r.getMove().equals(move))
                    .findFirst()
                    .orElse(null);

            score += result.getScore();
        };

        return score;
    }

}
