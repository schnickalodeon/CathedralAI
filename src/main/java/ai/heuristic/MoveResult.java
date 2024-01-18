package ai.heuristic;

import game_logic.Move;

public class MoveResult implements Comparable<MoveResult>{
    private final Move move;
    private final Float score;

    public MoveResult(Move move, float score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove(){return move;}
    public Float getScore(){return score;}


    @Override
    public int compareTo(MoveResult o) {
        return Float.compare(this.getScore(), o.getScore());
    }
}
