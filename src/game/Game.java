package game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.ChessBoardView;
import gui.ControlPane;
import gui.HistoryPane;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Player;
import model.Position;
import pieces.King;
import pieces.Piece;
import pieces.Rook;

public class Game {
    private Board board;
    private static Player whitePlayer;
    private static Player blackPlayer;
    private static Player currentPlayer;
    private boolean isGameOver;
    private static List<Move> moveHistory;
    private Timer whiteTimer;
    private Timer blackTimer;
    private ControlPane controlPane;
    
    public Game(Player whitePlayer, Player blackPlayer, Boolean isGamble, ControlPane controlPane) {
        this.board = new Board(isGamble);
        Game.whitePlayer = whitePlayer;
        Game.blackPlayer = blackPlayer;
        Game.currentPlayer = whitePlayer; 
        this.controlPane = controlPane;
        this.isGameOver = false;
        moveHistory = new ArrayList<>();
       
        this.whiteTimer = new Timer(5, 0); // 5 minutes for example
        this.blackTimer = new Timer(5, 0);
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
                Board tempBoard = board.copy(); 
                tempBoard.movePiece(piece.getPosition(), move);
                if (!isInCheckAfterMove(player.getColor(), tempBoard)) {
                    return false; 
                }
            }
        }
        return true; 
    }

    private boolean isInCheckAfterMove(String playerColor, Board tempBoard) {
        King king = tempBoard.findKing(playerColor);
        if (king == null) {
            return true; 
        }
        Position kingPos = king.getPosition();
        Player opponent = (playerColor.equals("white")) ? blackPlayer : whitePlayer;
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
            stopTimer(0);
            stopTimer(1);
            System.out.println("The game is over. No more moves can be made.");
            return;
        }

        try {
            Piece piece = board.getPieceAt(from);
            
            if (isPinned(piece, board, to)) {
                controlPane.updateGameText("This piece is pinned.");
                playSound("illegal");
                throw new IllegalArgumentException("This piece is pinned and cannot be moved.");
            } 
            
            if (isInCheck(currentPlayer)) {
                controlPane.updateGameText("Your king is in check");
                if (!canRemoveCheck(from, to)) {
                    throw new IllegalArgumentException("Cannot move, your king is in check. Move your king or block the check.");
                }
            }

            List<Position> validMoves = piece.getValidMoves(board);
            if (!validMoves.contains(to)) {
                controlPane.updateGameText("Invalid move.");
                playSound("illegal");
                throw new IllegalArgumentException("Invalid move.");
            }

            Piece capturedPiece = board.getPieceAt(to);
            if (capturedPiece != null) {
                playSound("Capture");  
            }
            
            Move move;
            if (piece instanceof King && ((to.getY() >=  from.getY()) ? to.getY() - from.getY() : from.getY() - to.getY()) == 2) {
                // Castling move
                Rook rook;
                Position rookFrom, rookTo;
                if (to.getY() > from.getY()) {
                    rookFrom = new Position(from.getX(), 7);
                    rookTo = new Position(from.getX(), 5);
                } else {
                    rookFrom = new Position(from.getX(), 0);
                    rookTo = new Position(from.getX(), 3);
                }
                rook = (Rook) board.getPieceAt(rookFrom);
                move = new CastlingMove((King) piece, rook, from, to, rookFrom, rookTo);
            } else {
                move = new StandardMove(piece, from, to);
            }
            
            controlPane.updateGameText("Moved.");
            move.execute(board);
            moveHistory.add(move);
            
            HistoryPane.updateHistory();
           
            Player opponent = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
            if (isInCheck(opponent)) {
                ChessBoardView.setCheckedKing(board.findKing(opponent.getColor()));
                playSound("Check");
                controlPane.updateGameText("Your king is in check");
                System.out.println(opponent.getName() + " is in check!");

                if (isCheckmate(opponent)) {
                    playSound("GameOver");
                    isGameOver = true;
                    controlPane.updateGameText("Checkmate! " + currentPlayer.getName() + " wins!");
                    System.out.println("Checkmate! " + currentPlayer.getName() + " wins!");
                }
            } else {
               
                if (isStalemate(opponent)) {
                    playSound("GameOver");
                    isGameOver = true;
                    controlPane.updateGameText("Stalemate! The game is a draw.");
                    System.out.println("Stalemate! The game is a draw.");
                }
            }
            
           if(!isInCheck(opponent) && !isStalemate(opponent) && capturedPiece == null) {
               if(move instanceof CastlingMove) {
                   playSound("Castling");
               } else {
                   playSound("Move");
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
    
    private boolean isPinned(Piece piece, Board board,Position targetPos) {
        King king = board.findKing(piece.getColor());
        if (king == null) return false; 

        Position kingPos = king.getPosition();
        Position piecePos = piece.getPosition();

        if (!isAligned(kingPos, piecePos)) {
            return false;
        }

        Board tempBoard = board.copy();
        tempBoard.movePiece(piecePos, targetPos);

        return isInCheckAfterMove(piece.getColor(), tempBoard);
    }

    private boolean isAligned(Position pos1, Position pos2) {
        return pos1.getX() == pos2.getX() || // Same row
               pos1.getY() == pos2.getY() || // Same column
               Math.abs(pos1.getX() - pos2.getX()) == Math.abs(pos1.getY() - pos2.getY()); // Same diagonal
    }

    private boolean canRemoveCheck(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) return false;

        Board tempBoard = board.copy();
        tempBoard.movePiece(from, to);

        King king = tempBoard.findKing(currentPlayer.getColor());
        if (king == null) return false;  

        return !isInCheckAfterMove(currentPlayer.getColor(), tempBoard);
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
                if (!isInCheckAfterMove(player.getColor(), tempBoard)) {
                    return false; 
                }
            }
        }
        
        return true;
    }
    
    public boolean isInCheck(Player player) {
        King king = board.findKing(player.getColor());
        if (king == null) {
            // Protect against missing king
            return false;
        }
        Position kingPos = king.getPosition();
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
        playerTimer.start();
        
        new Thread(() -> {
            try {
                while (!playerTimer.isTimerEmpty() && playerTimer.isRunning()) {
                    Thread.sleep(1000);  
                    playerTimer.decrementTimer(1);
                    
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

    public void stopTimer(int playerIndex) {
        Timer playerTimer = (playerIndex == 0) ? whiteTimer : blackTimer;
        playerTimer.stop(); 
    }

    private void endGame(String message) {
        isGameOver = true;
        System.out.println(message);
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        String s = (currentPlayer == whitePlayer) ? "White" : "Black";
        ControlPane.updateTurnIndicator(s);
        startTimer(currentPlayer == whitePlayer ? 0 : 1);  
        stopTimer(currentPlayer == whitePlayer ? 1 : 0);
    }

    public static List<Move> getMoveHistory() {
        return moveHistory;
    }
    
    private void playSound(String type) {
        try {
            String resourcePath = "/" + type + ".wav"; 
            java.net.URL resource = getClass().getResource(resourcePath);

            if (resource == null) {
                System.err.println("Sound file not found: " + resourcePath);
                return;
            }

            File tempFile = File.createTempFile(type, ".wav");
            tempFile.deleteOnExit(); 

            try (java.io.InputStream inputStream = resource.openStream();
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            Media sound = new Media(tempFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
