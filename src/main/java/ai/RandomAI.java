package ai;

import game_logic.*;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RandomAI extends AI {

    private static final Random random = new Random();


    @Override
    public Move getMove(Board board, Player player) {

        Point randomPoint = getRandomPoint();
        Building randomBuilding = getRandomBuilding(player.getBuildings());
        Direction randomDirection = getRandomDirection();

        return new Move(randomPoint, randomBuilding, randomDirection, player);
    }


    @Override
    public void printBestNumbers() {

    }

    private static Direction getRandomDirection() {
        int iRandom = random.nextInt(3);
        return Direction.values()[iRandom];
    }

    private static Building getRandomBuilding(List<Building> buildings) {
        int buildingCount = buildings.size();
        return buildings.get(random.nextInt(buildingCount));
    }

    private static Point getRandomPoint() {
        int x = random.nextInt(9);
        int y = random.nextInt(9);

        return new Point(x, y);
    }

}