import ai.AI;
import ai.CleverRandomAI;
import ai.DeterministicAI;
import ai.RandomAI;
import game_logic.Game;

public class Program {
    public static void main(String[] args) {
        AI alice =  new DeterministicAI();
        AI bob = new CleverRandomAI();
        Game game = new Game(alice, bob);
        game.start();
    }
}
