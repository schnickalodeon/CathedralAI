package game_logic.buildings;

import game_logic.Building;

import java.util.Comparator;

public class buildingSizeComparitor implements Comparator<Building>
{
    @Override
    public int compare(Building o1, Building o2) {
        return o1.getSize().compareTo(o2.getSize());
    }


}
