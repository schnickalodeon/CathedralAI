package ai;

import ai.heuristic.MoveResult;
import game_logic.Game;
import game_logic.Move;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DeterministicThreading implements Callable<MoveResult> {

    private final Game test;
    MoveResult m;
    AI ai;

    DeterministicThreading(AI ai, Game game, MoveResult m) {
        this.ai = new OtherDeterministicAI((OtherDeterministicAI) ai);
        this.test = new Game(game);
        this.m = m;
    }

    @Override
    public MoveResult call() {
        test.getActivePlayer().makeMove(m.getMove());
        test.getActivePlayer().removeBuildiung(m.getMove().getBuilding());
        test.getBoard().checkBoard(m.getMove().getPlayer().getColor());

        List<MoveResult> listOfGoodMoves;
        List<Move> nextPossibleMoves = test.getActivePlayer().generateValidMoves(test.getActivePlayer().getBuildings());
        listOfGoodMoves = ai.getBestMove(nextPossibleMoves, test, 5).stream()
                .filter(Objects::nonNull).toList();
        return new MoveResult(this.m.getMove(),
                listOfGoodMoves.stream()
                        .map(MoveResult::getScore)
                        .filter(Objects::nonNull)
                        .max((o1, o2) -> (int) (o1 - o2))
                        .orElse(0f));
    }

    private List<MoveResult> makeOneMove() {
        List<MoveResult> listOfGoodMoves;
        List<Move> nextPossibleMoves = test.getActivePlayer().generateValidMoves(test.getActivePlayer().getBuildings());
        listOfGoodMoves = ai.getBestMove(nextPossibleMoves, test, 30);
        MoveResult bestMove = listOfGoodMoves.stream().sorted(MoveResult::compareTo).toList().get(0);

        // dont check if the move is more than 5% worse than the best move found so far.
        double threshhold = bestMove.getScore() * 0.95;
        return listOfGoodMoves.stream().filter(moveResult -> moveResult.getScore() > threshhold).toList();

    }
}