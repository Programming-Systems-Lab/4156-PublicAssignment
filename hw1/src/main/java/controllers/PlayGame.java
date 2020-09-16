package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    GameBoard gameBoard = new GameBoard();
    Gson gson = new Gson();
    
    /* Get the Game by returning the initial UI thus allowing
     * the user (player 1) to start a new game.
     */
    app.get("/newgame", ctx -> {
      gameBoard.restart();
      ctx.redirect("/tictactoe.html");
    });
    
    /* Start the Game by initializing the board and 
     * setting up player 1.
     */
    app.post("/startgame", ctx -> {
      char type = ctx.formParam("type").charAt(0);
      Player player1 = new Player(type, 1);
      gameBoard.setPlayer1(player1);
      ctx.result(gson.toJson(gameBoard));
    });
    
    /* Join the game as player 2. If player 1 has yet to join or game has already
     * started, then report the appropriate message. Otherwise, redirect to page
     * and send the game board to both players.
     */
    app.get("/joingame", ctx -> {
      if (gameBoard.getPlayer1() == null) {
        // Player 1 has not join us
        Message message = new Message(false).createGameNotStartedMessage();
        ctx.result(gson.toJson(message));
        
      } else if (gameBoard.getGameStarted() == true) {
        // Player 1 has joined but game has already started
        Message message = new Message(false).createCannotJoinMessage();
        ctx.result(gson.toJson(message));
        
      } else {
        // Player 1 has joined and game hasn't started
        char type = gameBoard.getOppositeType();
        gameBoard.setPlayer2(new Player(type, 2));
        ctx.redirect("/tictactoe.html?p=2");
        sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      }
    });
    
    /* Make a Move by updating the game board if the move is valid.
     * Otherwise return the appropriate error message
     */
    app.post("/move/:playerId", ctx -> {
      Message message = null;
      int playerId = ctx.pathParam("playerId", Integer.class).get();
      
      // Checking conditions before making a move
      if (!gameBoard.everyoneIsHere()) {
        // Not everyone has joined the game
        message = new Message(false).createGameNotStartedMessage();
        ctx.result(gson.toJson(message));
        
      } else if (!gameBoard.isMyTurn(playerId)) {
        // Everyone has joined but it's not the player's turn
        message = new Message(false).createNotYourTurnMessage();
        ctx.result(gson.toJson(message));
        
      } else {
        // Everyone has joined and it's the player's turn
        int moveX = Integer.parseInt(ctx.formParam("x"));
        int moveY = Integer.parseInt(ctx.formParam("y"));
        Player player = gameBoard.getPlayer(playerId);
        Move move = new Move(player, moveX, moveY);
            
        // Check if move is valid
        if (gameBoard.isValidMove(move.getMoveX(), move.getMoveY())) {
          // Because move is valid, we can place mark on board
          gameBoard.makeMove(move);
          
          // After every move, we see if win, draw, or none (regular move)
          if (gameBoard.isWinner(player.getType())) {
            // Win
            message = new Message(true).createWinMessage(player.getId());
            gameBoard.setWinner(player.getId());
          } else if (gameBoard.thereIsADraw() == true) {
            // Draw
            message = new Message(true).createDrawMessage();
          } else {
            // Regular move - neither win nor draw
            message = new Message(true).createDefaultMessage();
          }
      
        // Move is not valid thus we respond with an invalid move message  
        } else {
          // System.out.println("not a valid move");
          message = new Message(false).createDefaultMessage();
        }
        ctx.result(gson.toJson(message));
        sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      }
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
