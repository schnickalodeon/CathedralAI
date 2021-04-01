package game_logic;

public enum Player {
    WHITE("White", false),  //TODO change DataType
    BLACK("Black", true);   //TODO change DataType

    final String name;
    final boolean value;
    Player(String name, boolean value) {
        this.name = name;
        this.value = value;
    }
}
