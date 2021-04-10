package game_logic;

import game_logic.buildings.Player;

import java.awt.*;

public enum FieldContent {
    EMPTY("Empty",(byte)0),
    CATHEDRAL("Cathedral",(byte)1),
    WHITE_OCCUPIED("Occupied by White",(byte)2),
    BLACK_OCCUPIED("Occupied by Black",(byte)3),
    WHITE_TERRITORY("White Territory",(byte)4),
    BLACK_TERRITORY("Black Territory",(byte)5);

    private final String name;
    private final byte value;

    FieldContent(String name,byte value) {
        this.name = name;
        this.value = value;
    }

    public static FieldContent getOccupiedByPlayer(Player player){
        PlayerColor color = player.getColor();
        switch (color){
            case BLACK:
                return BLACK_OCCUPIED;
            case WHITE:
                return WHITE_OCCUPIED;

            default:
                return EMPTY;
        }
    }

    public static Color getColor(FieldContent content){
        switch (content){
            case WHITE_OCCUPIED:
                return Color.WHITE;
            case BLACK_OCCUPIED:
                return Color.BLACK;
            default:
                return Color.CYAN;
        }
    }


}
