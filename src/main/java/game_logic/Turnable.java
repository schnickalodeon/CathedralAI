package game_logic;


public enum Turnable {
    Not(0),
    Half(2),
    Full(4);

    private final int turnable;
    Turnable(int turnable) {
        this.turnable = turnable;
    }

    public int getTurnable(){
        return turnable;
    }

}
