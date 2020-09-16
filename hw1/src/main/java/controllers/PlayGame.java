package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8084;

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
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    
    // Get Game -> Returns initial UI which allows user to start new game
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    // Start Game -> Initializes the board and setups player 1. 
    app.post("/startgame", ctx -> {
      char type = ctx.formParam("type").charAt(0);
      // System.out.println(type);
      Player player1 = new Player(type, 1);
      // System.out.println("I created player 1");
      gameBoard.setPlayer1(player1);
      // System.out.println("I added player1 to gameBoard");
      // gameBoard.printBoard();
      System.out.println(gameBoard.getGameStarted());
      ctx.result(gson.toJson(gameBoard));
    });
    
    // Join Game -> Adds player 2 to game board. Once in, the game starts
    app.get("/joingame", ctx -> {
      if (gameBoard.getGameStarted() == false) {
        char type = gameBoard.getOppositeType();
        // System.out.println(type);
        gameBoard.setPlayer2(new Player(type, 2));
        // System.out.println("I added player2 to gameBoard");
        // gameBoard.printBoard();
        System.out.println(gameBoard.getGameStarted());
        System.out.println(gson.toJson(gameBoard));
        ctx.redirect("/tictactoe.html?p=2");
        System.out.println("I'm about to send board");
        sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      } else {
        Message message = new Message(false).createCannotJoinMessage();
        ctx.result(gson.toJson(message));
      }
    });
    
    // Move -> Updates game-board if move is valid. Otherwise, returns error message
    app.get("/move/:playerId", ctx -> {
      int playerId = Integer.parseInt(ctx.queryParam("playerId"));
      int moveX = Integer.parseInt(ctx.formParam("x"));
      int moveY = Integer.parseInt(ctx.formParam("x"));
      
      Player player = gameBoard.getPlayer(playerId);
      Move move = new Move(player, moveX, moveY);
      Message message = null;
      
      // checking to see if valid move
      if (gameBoard.isValidMove(move.getMoveX(), move.getMoveY())) {
        // since move is valid, we make the move
        gameBoard.makeMove(player.getType(), move.getMoveX(), move.getMoveY());
        
        // with every move, we must see if draw, win, or none of the above
        if (gameBoard.thereIsADraw() == true) {
          message = new Message(true).createDrawMessage(); 
        } else if (gameBoard.isWinner(player.getType())) {
          message = new Message(true).createWinMessage(player.getId());
          gameBoard.setWinner(player.getId());
        } else {
          message = new Message(true).createDefaultMessage();
        }
      
      // not a valid move, so respond with invalid move message  
      } else {
        message = new Message(false).createDefaultMessage();
      }
      ctx.result(gson.toJson(message));
      sendGameBoardToAllPlayers(gson.toJson(gameBoard));
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
