package GUI;

import game_logic.Game;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class GUI
{
  private static PApplet processing;
  private Game game;

  public GUI()
  {
    PApplet.main("Program", null);
  }

public void setup(PApplet p, Game game) {
    processing = p;
    setGame(game);
}
public void setGame(Game game){
    this.game = game;
}
public void draw()
{

}
}
