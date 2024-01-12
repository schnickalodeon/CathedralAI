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

    public int getNumber() {
        return number;
    }

    public static Direction getDirectionByValue(int value) {
        switch (value) {
            case 0 -> {
                return D_0;
            }
            case 1 -> {
                return D_90;
            }
            case 2 -> {
                return D_180;
            }
            default -> {
                return D_270;
            }
        }
    }
}
