import ai.AI;
import ai.CleverRandomAI;
import ai.DeterministicAI;
import ai.RandomAI;
import game_logic.Game;

import java.util.Random;

public class Program {
    public static void main(String[] args) {

        Random r = new Random();
        //0.16381311 282.0
        AI alice =  new DeterministicAI(r.nextFloat(),r.nextInt(100), r.nextInt(10));
        AI bob = new CleverRandomAI();
        for(int i=0; i<10;i++)
        {
            Game game = new Game(alice, bob);
            boolean didWhitewin = game.start();

            if (didWhitewin) {
                bob = new DeterministicAI(r.nextFloat(), r.nextInt(1000),r.nextInt(50));
            } else {
                alice = new DeterministicAI(r.nextFloat(), r.nextInt(1000),r.nextInt(50));
            }
        }
        bob.printBestNumbers();
        alice.printBestNumbers();
    }
}
