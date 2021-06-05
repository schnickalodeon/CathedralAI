package GUI;

import game_logic.Game;
import game_logic.Player;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private static PApplet processing;

    private Game game;
    private Player Playerblack;
    private Player PlayerWhite;
    private Point playerPos;
    private List<Point> PlayerHover = new ArrayList<>();
    public boolean loop;

    public GUI() {
        PApplet.main("Program", null);
    }

    public void setup(PApplet p, Game game) {
        processing = p;
        p.frameRate(1);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayers(Game game) {
        Playerblack = game.getBlack();
        PlayerWhite = game.getWhite();
    }


    public void draw() {
        if (game != null) {
            loop = true;
            processing.background(0xf5, 0xE1, 0xC8);
            processing.fill(0xff);
            processing.textSize(20);
            processing.text("black:" + Playerblack.countPoints() + " White:" + PlayerWhite.countPoints(), 20, 20);
            int squareSize = ((processing.width - 200) / 10);
            for (int i = 0; i < 100; i++) {
                int content = game.getBoard().getContent(i).getValue();
                switch (content) {
                    case 0 -> {
                        processing.fill(0xED, 0xE2, 0xBF);
                    }
                    case 1 -> {
                        processing.fill(0x93, 0x73, 0x4A);
                    }
                    case 2 -> {
                        processing.fill(0xff);
                    }
                    case 3 -> {
                        processing.fill(0x0F, 0x0F, 0x0F, 200);
                    }
                    case 4 -> {
                        processing.fill(0xAA);
                    }
                    case 5 -> {
                        processing.fill(0x22);
                    }
                    default -> {
                    }
                }
                processing.strokeWeight(1f);
                processing.stroke(0x4B, 0x21, 0x01);

                processing.square(i % 10 * squareSize, 200 + i / 10 * squareSize, squareSize);
            }
            processing.fill(0, 0, 0xff);
            for (Point p : PlayerHover) {
                processing.square((p.x + playerPos.x) * squareSize, 200 + (p.y + playerPos.y) * squareSize, squareSize);
            }
        }
    }

    public Point checkForMovement(Point Position) {
        if (loop) {
            if (processing.keyPressed && processing.key == 'W' || processing.keyPressed && processing.key == 'w') {
                if (--Position.y < 0) {
                    Position.y = 9;
                }
                loop = false;
            }
            if (processing.keyPressed && processing.key == 'S' || processing.keyPressed && processing.key == 's') {
                Position.y++;
                if (Position.y > 10) {
                    Position.y = 0;
                }
                loop = false;
            }
            if (processing.keyPressed && processing.key == 'A' || processing.keyPressed && processing.key == 'a') {
                if (--Position.x < 0) {
                    Position.x = 9;
                }
                loop = false;
            }
            if (processing.keyPressed && processing.key == 'D' || processing.keyPressed && processing.key == 'd') {
                if (++Position.x > 10) {
                    Position.x = 0;
                }
                loop = false;
            }

        }
        return Position;
    }

    public PApplet getProcessing() {
        return processing;
    }

    public void playerHoveringBuilding(List<Point> shape, Point p) {
        PlayerHover = shape;
        playerPos = p;
    }

    public boolean syncLoops() {
        return loop;
    }

    public int checkForDirection(int directionPointer) {
        if (loop) {
            if (processing.keyPressed && processing.key == 'Q' || processing.keyPressed && processing.key == 'q') {
                if (--directionPointer <= 0) return 3;
                loop = false;
            }
            if (processing.keyPressed && processing.key == 'E' || processing.keyPressed && processing.key == 'e') {
                if (++directionPointer > 4) return 0;
                loop = false;
            }
        }

        return directionPointer;

    }

    public int selectBuilding(int buildingPointer, int listSize) {
        if (processing == null) return buildingPointer;
        if (processing.keyPressed && processing.key == 'Y' || processing.keyPressed && processing.key == 'y') {
            loop = false;
            if (++buildingPointer >= listSize) return 0;
        }
        if (processing.keyPressed && processing.key == 'X' || processing.keyPressed && processing.key == 'x') {
            loop = false;
            if (--buildingPointer < 0) return listSize - 1;
        }
        return buildingPointer;
    }
}

