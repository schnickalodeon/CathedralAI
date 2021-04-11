import ai.CleverRandomAI;
import ai.RandomAI;
import game_logic.Game;

public class Program {
    public static void main(String[] args) {
        RandomAI ai =  new RandomAI();
        CleverRandomAI ki = new CleverRandomAI();
        Game game = new Game(ki, ki);
        game.start();
    }
}
