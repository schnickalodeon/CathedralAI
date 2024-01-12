package ai;

import GUI.GUI;
import game_logic.*;
import processing.core.PApplet;

import java.awt.*;

public class ActuallyAHuman extends AI {
    Point Position;
    Building b;
    Direction d;
    GUI gui;
    PApplet p;

    @Override
    public Move getMove(Board board, Player player) {
        Position = new Point(5, 5);
        int directionPointer = 0;
        int buildingPointer = 0;

        d = Direction.getDirectionByValue(directionPointer);
        b = player.getBuildings().get(buildingPointer);

        while (p == null) {
            gui = player.getGame().getGui();
            p = gui.getProcessing();
        }

        while (true) {
            gui.playerHoveringBuilding(b.getShape(d), Position);
            Position = gui.checkForMovement(Position);
            directionPointer = gui.checkForDirection(directionPointer);
            buildingPointer = gui.selectBuilding(buildingPointer, player.getBuildings().size());
            d = Direction.getDirectionByValue(directionPointer);
            b = player.getBuildings().get(buildingPointer);
            if (p.keyPressed && p.keyCode == p.ENTER) {
                Move m = new Move(Position, b, d, player);
                if (player.isPlaceable(m)) {
                    return m;
                }
            }
        }
    }

    @Override
    public void printBestNumbers() {

    }
}
