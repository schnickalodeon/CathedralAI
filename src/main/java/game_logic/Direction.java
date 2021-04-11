package game_logic;

public enum Direction {
    D_0(0),
    D_90(1),
    D_180(2),
    D_270(3);

    private final int number;

    Direction(int number) {
        this.number = number;
    }
    public int getNumber(){return number;}

}
