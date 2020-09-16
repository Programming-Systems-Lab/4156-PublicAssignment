package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  /** Move Class Constructor.
   * A move is created after a player returns the X and Y coordinates 
   * of their most recent move
   * @param player - player who made the move
   * @param moveX - the X coordinate (zero-based)
   * @param moveY - the Y coordinate (zero-based)
   */
  public Move(Player player, int moveX, int moveY) {
    this.player = player;
    this.moveX = moveX;
    this.moveY = moveY;
  }
  
  /** Get Player.
   * Note: For debugging purposes
   * @return the player who made the move
   */
  public Player getPlayer() {
    return this.player;
  }
  
  /** Get Get moveX.
   * Note: For debugging purposes
   * @return the X-coordinate of their move
   */
  public int getMoveX() {
    return this.moveX;
  }
  
  /** Get Get moveX.
   * Note: For debugging purposes
   * @return the X-coordinate of their move
   */
  public int getMoveY() {
    return this.moveY;
  }

}
