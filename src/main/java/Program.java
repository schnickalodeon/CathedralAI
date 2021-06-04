import GUI.GUI;
import ai.ArtificialIntelligent;
import ai.CleverRandomAI;
import ai.DeterministicAI;
import game_logic.Game;
import processing.core.PApplet;

import java.util.Random;

public class Program extends  PApplet{
    public GUI gui;
    private static Game game;
    public static void main(String[] args) {
         gui = new GUI();
        Random r = new Random();
        //0.16381311 282.0
        ArtificialIntelligent alice =  new DeterministicAI(r.nextFloat(),r.nextInt(100), r.nextInt(10));
        ArtificialIntelligent bob = new CleverRandomAI();
        for(int i=0; i<10;i++)
        {
            game = new Game(alice, bob);
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

    public void settings() {
        size(800, 800);
    }

        public void setup() {
            gui.setup(this, game);
        }

        public void draw() {
            gui.draw();
        }

    }
}
