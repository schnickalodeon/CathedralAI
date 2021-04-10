package game_logic;


public enum Turnable {
    Not(1),
    Half(2),
    Full(4);

    private final int value;
    Turnable(int value) {
        this.value =value;
    }

    public int getValue(){
        return value;
    }

}
