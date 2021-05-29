package ai.heuristic;

import game_logic.Move;

public class MoveResult {
    private Move move;
    private Float score;

    public MoveResult(Move move, float score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove(){return move;}
    public Float getScore(){return score;}
}
