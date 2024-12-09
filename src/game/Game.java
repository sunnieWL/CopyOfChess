package game;

import model.Player;
import model.Position;
import pieces.Bishop;
import pieces.King;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.util.ArrayList;
import java.util.List;

import gui.ControlPane;
import gui.TimerPane;
import javafx.application.Platform;


public class Game {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private boolean isGameOver;
    private List<Move> moveHistory;
    private static boolean isGameStart;
    private static Timer[] playerTimer= new Timer[] {new Timer(1, 0, 0), new Timer(1, 0, 0)};
    // added 
    private static TimerPane[] timerPane;
    private volatile boolean[] timerThreadsRunning = new boolean[2];
    private boolean timersStarted = false;
    private static ControlPane controlPane;

    public Game(Player whitePlayer, Player blackPlayer) {
        this.board = new Board();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentPlayer = whitePlayer; 
        this.isGameOver = false;
        this.moveHistory = new ArrayList<>();
        
        controlPane = new ControlPane();
        initializeTimers(0, 1, 0);
    }

    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return isGameOver; }

    public boolean isCheckmate(Player player) {
        if (!isInCheck(player)) {
            return false;
        }

        List<Piece> pieces = board.getAllPieces(player.getColor());
        for (Piece piece : pieces) {
            List<Position> moves = piece.getValidMoves(board);
            for (Position move : moves) {
                // สร้างบอร์ดใหม่เพื่อคำนวณว่าหนรหรือกันได้ไหม
                Board tempBoard = board.copy(); 
                tempBoard.movePiece(piece.getPosition(), move);
                //(ไลเรื่อยๆทุกรูปแบบ)
                if (!isInCheckAfterMove(player, tempBoard)) {
                    return false; 
                }
            }
        }
        return true; // หนีไม่ได้
    }

    private boolean isInCheckAfterMove(Player player, Board tempBoard) {
        King king = tempBoard.findKing(player.getColor());
        if (king == null) {
            return true; 
        }
        Position kingPos = king.getPosition();
        Player opponent = (player == whitePlayer) ? blackPlayer : whitePlayer;
        List<Piece> opponentPieces = tempBoard.getAllPieces(opponent.getColor());
        for (Piece piece : opponentPieces) {
            if (piece.getValidMoves(tempBoard).contains(kingPos)) {
                return true;
            }
        }
        return false;
    }

    public void makeMove(Position from, Position to) {
        if (isGameOver) {
            System.out.println("The game is over. No more moves can be made.");
            return;
        }

        try {
            Piece piece = board.getPieceAt(from);
            
            if (isPinned(piece, board)) {
                throw new IllegalArgumentException("This piece is pinned and cannot be moved.");
            } 
            
            if (isInCheck(currentPlayer)) {
                if (!canRemoveCheck(from, to)) {
                    throw new IllegalArgumentException("Cannot move, your king is in check. Move your king or block the check.");
                }
            }

            List<Position> validMoves = piece.getValidMoves(board);
            if (!validMoves.contains(to)) {
                throw new IllegalArgumentException("Invalid move.");
            }

            Move move = new StandardMove(piece, from, to);
            move.execute(board);
            moveHistory.add(move);
            
            if (!timersStarted) {
                timersStarted = true;
               
                startCountDownTimer(0); // White's timer
                startCountDownTimer(1); // Black's timer
            }
            
            Player opponent = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
            if (isInCheck(opponent)) {
                
                System.out.println(opponent.getName() + " is in check!");

                if (isCheckmate(opponent)) {
                    isGameOver = true;
                    System.out.println("Checkmate! " + currentPlayer.getName() + " wins!");
                }
            } else {
               
                if (isStalemate(opponent)) {
                    isGameOver = true;
                    System.out.println("Stalemate! The game is a draw.");
                }
            }

            if (!isGameOver) {
            	
                switchPlayer();
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while making the move: " + e.getMessage());
        }
    }
    
    private boolean isPinned(Piece piece, Board board) {
        King king = board.findKing(piece.getColor());
        if (king == null) return false;

        Position kingPos = king.getPosition();
        List<Piece> opponentPieces = board.getAllPieces(piece.getColor().equals("white") ? "black" : "white");

        for (Piece opponentPiece : opponentPieces) {
            // เช็ค same lin
            if (opponentPiece instanceof Rook || opponentPiece instanceof Bishop || opponentPiece instanceof Queen) {
                if (isPiecePinningKing(opponentPiece, piece, kingPos, board)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isPiecePinningKing(Piece opponentPiece, Piece piece, Position kingPos, Board board) {
        Position opponentPiecePos = opponentPiece.getPosition();
        
        // same r or c
        if (opponentPiece instanceof Rook || opponentPiece instanceof Queen) {
            if (opponentPiecePos.getX() == kingPos.getX() || opponentPiecePos.getY() == kingPos.getY()) {
            	return isPieceInPath(piece, opponentPiecePos, kingPos, board);
            }
        }
        if (opponentPiece instanceof Bishop || opponentPiece instanceof Queen) {
            if (Math.abs(opponentPiecePos.getX() - kingPos.getX()) == Math.abs(opponentPiecePos.getY() - kingPos.getY())) {
            	return isPieceInPath(piece, opponentPiecePos, kingPos, board);
            }
        }
        return false;
    }
    
    private boolean isPieceInPath(Piece piece, Position opponentPiecePos, Position kingPos, Board board) {
        int dx = Integer.signum(kingPos.getX() - opponentPiecePos.getX());
        int dy = Integer.signum(kingPos.getY() - opponentPiecePos.getY());

        int x = opponentPiecePos.getX() + dx;
        int y = opponentPiecePos.getY() + dy;

        while (x != kingPos.getX() || y != kingPos.getY()) {
            if (board.getPieceAt(x, y) != null) {
                if (board.getPieceAt(x, y).equals(piece)) {
                    return true; 
                } else {
                    return false; 
                }
            }
            x += dx;
            y += dy;
        }
        return false; 
    }
    
    private boolean canRemoveCheck(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) return false;

        Board tempBoard = board.copy();
        tempBoard.movePiece(from, to);

        King king = tempBoard.findKing(currentPlayer.getColor());
        if (king == null) return false;  

        return !isInCheckAfterMove(currentPlayer, tempBoard);
    }
    
    

    public boolean isStalemate(Player player) {
        if (isInCheck(player)) {
            return false;
        }

        List<Piece> pieces = board.getAllPieces(player.getColor());
        for (Piece piece : pieces) {
            List<Position> moves = piece.getValidMoves(board);
            for (Position move : moves) {
                Board tempBoard = board.copy(); 
                tempBoard.movePiece(piece.getPosition(), move);
                if (!isInCheckAfterMove(player, tempBoard)) {
                    return false; 
                }
            }
        }
        
        return true;
    }
    
    public boolean isInCheck(Player player) {
        King king = board.findKing(player.getColor());
        if (king == null) {
            // กันเผื่อ
            return false;
        }
        Position kingPos = king.getPosition();
        // มีตัวเช็คคิงไหม
        Player opponent = (player == whitePlayer) ? blackPlayer : whitePlayer;
        List<Piece> opponentPieces = board.getAllPieces(opponent.getColor());
        for (Piece piece : opponentPieces) {
            List<Position> moves = piece.getValidMoves(board);
            if (moves.contains(kingPos)) {
                return true;
            }
        }
        return false;
    }
    
    public void startCountDownTimer(int pl) {
        synchronized (this) {
               if (timerThreadsRunning[pl]) return;

               timerThreadsRunning[pl] = true; 
           }

        if (!timersStarted) {
            timersStarted = true;
            startCountDownTimer(0); // White's timer
            startCountDownTimer(1); // Black's timer
            System.out.println("Timers started for both players");
        }
        new Thread(() -> {
            try {
                runCountDownTimer(pl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
       
       public void runCountDownTimer(int pl) throws InterruptedException {
        
        
           Timer plTimer = playerTimer[pl];
           plTimer.setStop(false);
           timerThreadsRunning[pl] = true;


           
           while (Game.isGameStart && timerThreadsRunning[pl] && getCurrentPlayer() == (pl == 0 ? whitePlayer : blackPlayer) && !plTimer.isTimerEmpty()) {
               Thread.sleep(100);  // Sleep for 1 second to simulate countdown
               System.out.println("Current Timer: " + plTimer.toString());
               Platform.runLater(() -> {
                   if (timerPane[pl] != null) {
                       timerPane[pl].setTimer(plTimer);
                   }
               }); 


               plTimer.decrementTimer(1);  // Decrement by 1 second
           }

           plTimer.setStop(true);
           if (plTimer.isTimerEmpty()) {
               endGame(pl == 0 ? "Black wins!" : "White wins!");
           }
       }
    
       public void endGame(String message) {
           isGameOver = true;
           isGameStart = false;
           

           Platform.runLater(() -> controlPane.updateGameText(message));
           for (int i = 0; i < timerThreadsRunning.length; i++) {
               stopTimer(i);
           }
       }
    
       private void initializeTimers(int hours, int minutes, int seconds) {
           playerTimer = new Timer[] {new Timer(hours, minutes, seconds), new Timer(hours, minutes, seconds)};
           timerPane = new TimerPane[2];

           for (int i = 0; i < 2; i++) {
               timerPane[i] = new TimerPane(i);
               timerPane[i].setTimer(playerTimer[i]);

           }
       }
    public void stopTimer(int pl) {
           timerThreadsRunning[pl] = false;
    }
    
    public static Timer getPlayerTimer(int pl) {
    	return playerTimer[pl];
    }

    public static void setTimerPane(int pl, TimerPane timerPane) {
    	Game.timerPane[pl] = timerPane;
    }
    
    public static ControlPane getControlPane() {
    	return controlPane;
    }
    
    
    public List<Piece> getAllPieces(String color) {
        return board.getAllPieces(color);
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        System.out.println("Switched player to: " + currentPlayer);

        // Make sure the correct player's timer is active
        if (currentPlayer == whitePlayer) {
            stopTimer(1);  // Stop black's timer
            startCountDownTimer(0);  // Start white's timer
        } else {
            stopTimer(0);  // Stop white's timer
            startCountDownTimer(1);  // Start black's timer
        }
    }

}
