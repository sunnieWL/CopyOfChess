package game;

import java.util.ArrayList;
import java.util.List;

import gui.ControlPane;
import gui.GamblePane;
import javafx.application.Platform;
import model.Player;
import model.Position;
import pieces.Bishop;
import pieces.King;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Knight;
import pieces.Pawn;

public class GambleGame extends Game {
    private GamblePane gamblePane;  // Added GamblePane instance
    private List<Piece> availablePieces;
    
    public GambleGame(Player whitePlayer, Player blackPlayer) {
        super(whitePlayer, blackPlayer);  // Initialize the base game
        this.gamblePane = new GamblePane(whitePlayer.getColor());  // Initialize GamblePane with white player's color
        updateAvailablePieces();  // Set up the available pieces for the first player
    }

    public Board getBoard() { return board; }
    public static Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return isGameOver; }

    public boolean isCheckmate(Player player) {
        if (board.findKing(player.getColor()) == null) {
            isGameOver = true;
            return true;
        }
        return false;
    }

    @Override
    public void makeMove(Position from, Position to) {
        if (isGameOver) {
            System.out.println("The game is over. No more moves can be made.");
            return;
        }

        try {
            Piece piece = board.getPieceAt(from);
            if (piece == null) {
                throw new IllegalArgumentException("No piece at the selected position.");
            }

            // Check if the piece is part of the available pieces for the current player by color and type
            boolean pieceIsAvailable = false;
            for (Piece availablePiece : availablePieces) {
                if (availablePiece.getClass().equals(piece.getClass()) && availablePiece.getColor().equals(piece.getColor())) {
                    pieceIsAvailable = true;
                    break;
                }
            }

            if (!pieceIsAvailable) {
                throw new IllegalArgumentException("You can only move one of your selected pieces.");
            }

            List<Position> validMoves = piece.getValidMoves(board);
            if (!validMoves.contains(to)) {
                throw new IllegalArgumentException("Invalid move.");
            }

            // Execute the move
            Move move = new StandardMove(piece, from, to);
            move.execute(board);
            moveHistory.add(move);

            // Check for game end conditions
            if (isCheckmate(getOpponent(currentPlayer))) {
                isGameOver = true;
                System.out.println("Checkmate! " + currentPlayer.getName() + " wins!");
            }

            // Switch to the next player
            switchPlayer();
            updateAvailablePieces();  // Update the available pieces for the new player

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while making the move: " + e.getMessage());
        }
    }

    // Helper method to get the opponent player
    private Player getOpponent(Player player) {
        return (player == whitePlayer) ? blackPlayer : whitePlayer;
    }
    
    private void updateAvailablePieces() {
        // Get the list of available pieces based on the current player's color
        availablePieces = getAvailablePiecesForCurrentPlayer();
        
        // Pass the available pieces to the GamblePane
        gamblePane.setAvailablePieces(availablePieces);
        gamblePane.setCurrentPlayerColor(currentPlayer.getColor());  // Update the GamblePane with the current player's color
    }

    public GamblePane getGamblePane() {
        return gamblePane;  // Provide access to the gamble pane
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

    /**
     * Returns the available pieces for the current player.
     */
    private List<Piece> getAvailablePiecesForCurrentPlayer() {
        List<Piece> pieces = new ArrayList<>();
        if (currentPlayer.getColor().equals("white")) {
            pieces.add(new Queen("white", null));
            pieces.add(new Rook("white", null));
            pieces.add(new Bishop("white", null));
            pieces.add(new Knight("white", null));
            pieces.add(new Pawn("white", null));  // You can add more pieces here as needed
        } else {
            pieces.add(new Queen("black", null));
            pieces.add(new Rook("black", null));
            pieces.add(new Bishop("black", null));
            pieces.add(new Knight("black", null));
            pieces.add(new Pawn("black", null));  // You can add more pieces here as needed
        }
        return pieces;
    }
}
