import GUI.GUI;
import ai.*;
import game_logic.Game;
import processing.core.PApplet;

import java.util.Random;

public class Program extends PApplet {
    public static GUI gui;
    private static Game game;

    public static void main(String[] args) {
        gui = new GUI();
        Random r = new Random();
        ArtificialIntelligent alice = new ActuallyAHuman();
        ArtificialIntelligent bob = new OtherDeterministicAI(r.nextFloat(),r.nextFloat());
        for (int i = 0; i < 100; i++) {
            game = new Game(bob, alice, gui);
            gui.setPlayers(game);
            boolean didWhitewin = game.start();

            if (didWhitewin) {
                bob = new DeterministicAI(r.nextFloat(), r.nextFloat(), r.nextFloat());
            } else {
                alice = new OtherDeterministicAI(r.nextFloat(),r.nextFloat());
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
