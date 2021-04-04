package game_logic;

public enum PlayerColor {
    WHITE("White", (short)0),
    BLACK("Black", (short)1),
    NEUTRAL("Neutral", (short)2);

    final String name;
    final short value;
    PlayerColor(String name, short value) {
        this.name = name;
        this.value = value;
    }

}
