package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  /** Message Class Constructor.
   * A message will be returned to the player at the end of their turn.
   * Only the moveValidity parameter is necessary for construction.
   * Note: "code" and "message" variables are assigned in methods below
   *  code - status code of move; 200 for valid and 403 for invalid
   *  message - string reflecting the win/draw/valid/invalid status of move
   * @param moveValidity - True if move is valid. False otherwise
   */
  public Message(boolean moveValidity) {
    this.moveValidity = moveValidity;
    this.code = 0;
    this.message = "";
  }
  
  /** Get Message's code.
   * Note: This is for debugging purposes
   * @return the (status) code
   */
  public int getCode() {
    return this.code;
  }
  
  /** Get Message's message.
   * Note: This is for debugging purposes
   * @return the (string) message
   */
  public String getMessage() {
    return this.message;
  }
  
  /** Create Game Not Started Messsage.
   * Generated when either player hasn't joined the game
   * @return this message
   */
  public Message createGameNotStartedMessage() {
    this.code = 403;
    this.message = "Please wait. Not all players have joined game";
    return this;
  }
  
  /** Create Draw (i.e. Tie) Message.
   * Since move was valid, set the code to 200 and 
   * return a simple message informing of Tie
   * @return this message
   */
  public Message createDrawMessage() {
    this.code = 200;
    this.message = "There is a Draw";
    return this;
  }
  
  /** Create Can't Move Message.
   * Generated when it's not the player's turn
   * @return this message
   */
  public Message createNotYourTurnMessage() {
    this.code = 403;
    this.message = "Not your turn";
    return this;
  }
  
  /** Create Win Message.
   * Since move was valid, set the code to 200 and 
   * return a simple message informing of player's win 
   * @param id - id of player
   * @return this message
   */
  public Message createWinMessage(int id) {
    this.code = 200;
    this.message = String.format("Player %d Win!", id);
    return this;
  }
  
  /** Create Default Message.
   * If not win or draw, return a default message informing the player 
   * that their move was valid (code=200) or invalid (code=403)
   * @return this message
   */
  public Message createDefaultMessage() {
    if (this.moveValidity == true) {
      this.code = 200;
      this.message = "Valid Move";
    } else {
      this.code = 403;
      this.message = "Invalid Move. Please try somewhere else.";
    }
    return this;
  }
  
  /** Create Cannot Join Message.
   * If the game hasn't started, inform the player attempting to join in
   * @return this message
   */
  public Message createCannotJoinMessage() {
    this.code = 403;
    this.message = "Cannot join as game has already started.";
    return this;
  }
  
}
