package models;

public class Player {

  private char type;

  private int id;
    
  /** Player Class Constructor.
   * Create a player for the Tic-Tac-Toe Game
   * @param type - symbol for game; either X or O
   * @param id - id of player; either 1 or 2
   */
  public Player(char type, int id) {
    this.type = type;
    this.id = id;
  }
  
  /** Get Player's Type.
   * @return this player's type
   */
  public char getType() {
    return this.type;
  }
  
  /** Get Player's ID.
   * @return this player's id
   */
  public int getId() {
    return this.id;
  }
    
}