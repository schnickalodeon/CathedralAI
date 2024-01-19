import GUI.GUI;
import ai.ActuallyAHuman;
import ai.ArtificialIntelligent;
import ai.DeterministicAI;
import ai.OtherDeterministicAI;
import game_logic.Game;
import processing.core.PApplet;

import java.util.Random;

public class Program /* extends PApplet*/{

    public static void main(String[] args) {
        Random r = new Random();
        ArtificialIntelligent alice = new OtherDeterministicAI(r.nextFloat(), r.nextFloat());
        ArtificialIntelligent bob = new OtherDeterministicAI(r.nextFloat(), r.nextFloat());
        for (int i = 0; i < 10; i++) {
            //public static GUI gui;
            Game game = new Game(bob, alice);
            boolean didWhitewin = game.start();

            if (didWhitewin) {
                bob = alice;
                alice = new OtherDeterministicAI(r.nextFloat(), r.nextFloat());
            } else {
                alice = new OtherDeterministicAI(r.nextFloat(), r.nextFloat());
            }
        }
        bob.printBestNumbers();
        alice.printBestNumbers();
    }

   /* public void settings() {
        size(800, 800);
    }

    public void setup() {
        gui.setup(this, game);
    }

    public void draw() {
        gui.draw();
    }*/

}
