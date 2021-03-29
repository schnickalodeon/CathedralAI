package game_logic;

public enum FieldContent {
    Empty("Empty",(byte)0),
    Cathedral("Cathedral",(byte)1),
    WhiteOccupied("Occupied by White",(byte)2),
    BlackOccupied("Occupied by Black",(byte)3),
    WhiteTerritory("White Territory",(byte)4),
    BlackTerritory("Black Territory",(byte)5);

    private final String name;
    private final byte value;

    FieldContent(String name,byte value) {
        this.name = name;
        this.value = value;
    }
}
