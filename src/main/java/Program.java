import ai.RandomAI;
import game_logic.Game;

public class Program {
    public static void main(String[] args) {
        RandomAI ai =  new RandomAI();
        Game game = new Game(ai, ai);
        game.Start();
    }
}
