package game_logic;

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
}
