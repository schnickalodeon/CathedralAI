package ai;

import ai.heuristic.MoveResult;
import game_logic.Game;
import game_logic.Move;

import javax.print.attribute.standard.MediaSize;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class DeterministicThreading implements Callable<MoveResult> {

    private Game test;
    MoveResult m;
    AI ai;

    DeterministicThreading(AI ai, Game game, MoveResult m) {
        this.ai = new OtherDeterministicAI((OtherDeterministicAI)ai);
        this.test = new Game(game);
        this.m = m;
    }

    @Override
    public MoveResult call() throws Exception {
        test.getActivePlayer().makeMove(m.getMove());
        test.getActivePlayer().removeBuildiung(m.getMove().getBuilding());
        test.getBoard().checkArea(m.getMove().getPlayer().getColor());

        List<MoveResult> listOfGoodMoves;
        List<Move> nextPossibleMoves = test.getActivePlayer().generateValidMoves(test.getActivePlayer().getBuildings());
        listOfGoodMoves = ai.getBestMove(nextPossibleMoves, test, 3);

        List<MoveResult> validResults = listOfGoodMoves.stream().filter(m -> m != null).collect(Collectors.toList());
        double sum = validResults.stream().mapToDouble(m -> m.getScore()).sum();
        sum /= validResults.size();
        return new MoveResult(m.getMove(), (float) sum);
    }
}