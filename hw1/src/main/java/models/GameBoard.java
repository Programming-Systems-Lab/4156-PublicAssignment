package models;

import java.util.Arrays;

public class GameBoard {

  private Player p1;
  private Player p2;
  private boolean gameStarted;
  private int turn;
  private char[][] boardState;
  private int winner;
  private boolean isDraw;
  
  /** GameBoard Class Constructor.
   * A standard 3x3 game board for two players. The attributes
   * are set for us during initialization.
   *   p1 - player 1 object
   *   p2 - player 2 object
   *   gameStarted - True if game has started. False otherwise
   *   turn - value that increases when a valid move is made.
   *   boardState - 3x3 array of array for storing player's marks on board
   *   winner - will contain the id>0 for player who won the game
   *   isDraw - True if game has a tie. False otherwise
   */
  public GameBoard() {
    this.p1 = null;
    this.p2 = null;
    this.gameStarted = false;
    this.turn = 1;
    this.boardState = new char[3][3];
    this.winner = 0;
    this.isDraw = false;
  }

  // Returns the player 1 object
  public Player getPlayer1() {
    return this.p1;
  }
  
  // Returns the player 2 object
  public Player getPlayer2() {
    return this.p2;
  }
  
  /** Get the Player's ID.
   * Match the id to the appropriate player and player obj
   * @param id - the id belonging to the player
   * @return the player object
   */
  public Player getPlayer(int id) {
    if (this.p1.getId() == id) {
      return p1;
    } else {
      return p2;
    }
  }

  /** Get the Opposite Type for Player 2.
   * If player 1's type is 'X', return '0' for player 2. 
   * Otherwise, return 'X' for player 2
   * @return the opposite type
   */
  public char getOppositeType() {
    char p1Type = this.p1.getType();
    if (p1Type == 'X') {
      return 'O';
    } else {
      return 'X';
    }
  }

  /** Set Player 1.
   * Set gameboard's player 1
   * @param player1 - player 1 object
   */
  public void setPlayer1(Player player1) {
    this.p1 = player1;
  }

  /** Setup Player 2.
   * Set gameboard's player 1 and set gameStarted as true
   * since player 2 has joined the game
   * @param player2 - player 2 object
   */
  public void setPlayer2(Player player2) {
    this.p2 = player2;
    this.gameStarted = true;
  }
  
  /** Get GameStarted.
   * @return the game's status
   */
  public boolean getGameStarted() {
    return this.gameStarted;
  }

  /** Check if move is valid.
   * Returns True if player's move is valid (i.e. there is
   * an open spot in the board. False otherwise
   * @param x - x coordinate of move
   * @param y - y coordinate of move
   * @return appropriate boolean value mentioned above
   */
  public boolean isValidMove(int x, int y) {
    if (this.boardState[x][y] == 0) {
      return true;
    } else {
      return false;
    }
  }

  /** Make Move.
   * This function is called when player's move is valid, 
   * meaning there is an open spot in board. Assign's the open
   * spot the player's symbol
   * @param symbol - the player's symbol; either 'X' or 'O'
   * @param x - x coordinate of player's move
   * @param y - y coordinate of player's move
   */
  public void makeMove(char symbol, int x, int y) {
    this.boardState[x][y] = symbol;
    this.turn += 1;
  }

  /** Are There More Available Moves.
   * Iterates the board to find an open spot
   * Note: for debugging purposes
   * @return True if board has an open spot. False otherwise.
   */
  public boolean moreAvailableMoves() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.boardState[i][j] == 0) {
          return true;
        }
      }
    }
    return false;
  }

  /** Is There A Winner.
   * Iterates the board and checks all rows, column, and length-3 diagonals
   * to check if there is a length-3 line all filled with the same symbol (type)
   * @param symbol - the player's type
   * @return True if player has won the game.
   */
  public boolean isWinner(char symbol) {
    int matchesHorizontal = 0;
    int matchesVertical = 0;
    int matchesLeftToRight = 0;
    int matchesRightToLeft = 0;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.boardState[i][j] == symbol) {
          matchesHorizontal += 1;
        }
        if (this.boardState[i][0] == symbol) {
          matchesVertical += 1;
        }
        if (this.boardState[i][i] == symbol) {
          matchesLeftToRight += 1;
        }
        if (this.boardState[i][2 - i] == symbol) {
          matchesRightToLeft += 1;
        }
      }

      if (matchesHorizontal == 3 || matchesVertical == 3 
          || matchesLeftToRight == 3 || matchesRightToLeft == 3) {
        return true;
      } else {
        matchesHorizontal = 0;
        matchesVertical = 0;
        matchesLeftToRight = 0;
        matchesRightToLeft = 0;
      }
    }
    return false;
  }

  /** Print Board.
   * Prints the entire 3x3 board to view current open/taken spots 
   * Note: For debugging purposes
   */
  public void printBoard() {
    for (int i = 0; i < this.boardState.length; i++) {
      System.out.println(Arrays.toString(this.boardState[i]));
    }
  }

  /** Is There a Draw.
   * If there are less than 9 moves (since board has only 9 spots) and no 
   * more available moves, then update the isDraw status and return its value 
   * @return True is draw. False otherwise
   */
  public boolean thereIsADraw() {
    if ((this.turn > 9)) {
      this.isDraw = true;
      return true;
    } else {
      this.isDraw = false;
      return false;
    }
  }
  
  /** Get IsDraw Status.
   * Note: For debugging purposes
   * @return boolean if there is a draw or not.
   */
  public boolean getIsDraw() {
    return this.isDraw;
  }

  /** Get Winner.
   * @return the id of player who won
   */
  public int getWinner() {
    return this.winner;
  }

  /** Set Winner.
   * Declare the winner of the game via player's id.
   */
  public void setWinner(int id) {
    this.winner = id;
  }

}
