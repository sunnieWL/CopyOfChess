package game;

import java.util.ArrayList;
import java.util.List;

import gui.ControlPane;
import javafx.application.Platform;
import model.Player;
import model.Position;
import pieces.Bishop;
import pieces.King;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Game {
    private Board board;
    private static Player whitePlayer;
    private static Player blackPlayer;
    private static Player currentPlayer;
    private boolean isGameOver;
    private List<Move> moveHistory;
    private Timer whiteTimer;
    private Timer blackTimer;

    public Game(Player whitePlayer, Player blackPlayer) {
        this.board = new Board();
        Game.whitePlayer = whitePlayer;
        Game.blackPlayer = blackPlayer;
        Game.currentPlayer = whitePlayer; 
        this.isGameOver = false;
        this.moveHistory = new ArrayList<>();
       
        this.whiteTimer = new Timer(5, 0); // 5 minutes for example
        this.blackTimer = new Timer(5, 0);
    }

    public Board getBoard() { return board; }
    public static Player getCurrentPlayer() { return currentPlayer; }
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

            Move move;
            if (piece instanceof King && Math.abs(to.getY() - from.getY()) == 2) {
                // Castling move
                Rook rook;
                Position rookFrom, rookTo;
                if (to.getY() > from.getY()) {
                    // Kingside
                    rookFrom = new Position(from.getX(), 7);
                    rookTo = new Position(from.getX(), 5);
                } else {
                    // Queenside
                    rookFrom = new Position(from.getX(), 0);
                    rookTo = new Position(from.getX(), 3);
                }
                rook = (Rook) board.getPieceAt(rookFrom);
                move = new CastlingMove((King) piece, rook, from, to, rookFrom, rookTo);
            } else {
                // Standard move
                move = new StandardMove(piece, from, to);
            }
            
            move.execute(board);
            moveHistory.add(move);
            
           
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
        
        boolean ans = false;
        
        while (x != kingPos.getX() || y != kingPos.getY()) {
            if (board.getPieceAt(x, y) != null) {
                if (board.getPieceAt(x, y).equals(piece)) {
                    ans = true; 
                } else {
                    return false; 
                }
            }
            x += dx;
            y += dy;
        }
        return ans; 
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
    
 
    
    
    public List<Piece> getAllPieces(String color) {
        return board.getAllPieces(color);
    }
    
    public void startTimer(int playerIndex) {
        Timer playerTimer = (playerIndex == 0) ? whiteTimer : blackTimer;

        // Start the timer for the selected player
        playerTimer.start();

        new Thread(() -> {
            try {
                while (!playerTimer.isTimerEmpty() && playerTimer.isRunning()) {
                    Thread.sleep(1000);  // Decrease timer by one second
                    playerTimer.decrementTimer(1);

                    // Update the GUI
                    Platform.runLater(() -> {
                        ControlPane.updateTimer(playerIndex, playerTimer);
                    });
                }
                if (playerTimer.isTimerEmpty()) {
                    endGame(playerIndex == 0 ? "Black wins!" : "White wins! Time out.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Stop the timer for the current player
    public void stopTimer(int playerIndex) {
        Timer playerTimer = (playerIndex == 0) ? whiteTimer : blackTimer;
        playerTimer.stop(); // Stop the respective player's timer
    }

    // Handle game end logic (e.g., timeout, checkmate, etc.)
    private void endGame(String message) {
        isGameOver = true;
        System.out.println(message);
        // Optionally update the GUI or handle other game-over procedures.
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        String s = (currentPlayer == whitePlayer) ? "White" : "Black";
        ControlPane.updateTurnIndicator(s);
        startTimer(currentPlayer == whitePlayer ? 0 : 1);  
        stopTimer(currentPlayer == whitePlayer ? 1 : 0);
        
    }

}