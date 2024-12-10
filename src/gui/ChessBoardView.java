package gui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import game.Board;
import game.Game;
import game.Move;
import interfaces.Drawable;
import model.Player;
import model.Position;
import pieces.King;
import pieces.Piece;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardView extends GridPane {
    private static final int TILE_SIZE = 80;
    private Board board;
    private Game game;
    private Position selectedPosition = null;
    private Rectangle[][] tiles = new Rectangle[8][8];
    private PieceView[][] pieceViews = new PieceView[8][8];
    private List<Drawable> drawables = new ArrayList<>();
    private List<Rectangle> highlightedTiles = new ArrayList<>();
    private static King king;

    public ChessBoardView(Board board, Game game) {
        this.board = board;
        this.game = game;
        drawBoard();
        drawPieces();
        ChessBoardView.king = null;
    }

    private void drawBoard() {
        for (int i = 0; i < 8; i++) {
            Text rowLabel = new Text(String.valueOf(8 - i));
            rowLabel.setStyle("-fx-font-weight: bold; -fx-fill: black;");
            add(rowLabel, 0, i + 1); 
        }

        for (int j = 0; j < 8; j++) {
            Text columnLabel = new Text(String.valueOf((char) ('a' + j)));
            columnLabel.setStyle("-fx-font-weight: bold; -fx-fill: black;");
            add(columnLabel, j + 1, 9); 
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((i + j) % 2 == 0) {
                    tile.setFill(Color.BEIGE);
                } else {
                    tile.setFill(Color.BROWN);
                }
                tiles[i][j] = tile;
                add(tile, j + 1, i + 1); 

                int x = i;
                int y = j;
                tile.setOnMouseClicked(event -> handleTileClick(x, y));
            }
        }
    }

    private void drawPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    PieceView pieceView = createPieceView(piece);
                    pieceViews[i][j] = pieceView;

                    add(pieceView.getText(), j + 1, i + 1);

                    drawables.add(pieceView);
                    pieceView.draw();
                }
            }
        }
    }

    private PieceView createPieceView(Piece piece) {
        if (piece instanceof pieces.King) {
            return new KingView((pieces.King) piece);
        } else if (piece instanceof pieces.Queen) {
            return new QueenView((pieces.Queen) piece);
        } else if (piece instanceof pieces.Rook) {
            return new RookView((pieces.Rook) piece);
        } else if (piece instanceof pieces.Bishop) {
            return new BishopView((pieces.Bishop) piece);
        } else if (piece instanceof pieces.Knight) {
            return new KnightView((pieces.Knight) piece);
        } else if (piece instanceof pieces.Pawn) {
            return new PawnView((pieces.Pawn) piece);
        } else {
            throw new IllegalArgumentException("Unknown piece type");
        }
    }

    private void clearPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieceViews[i][j] != null) {
                    getChildren().remove(pieceViews[i][j].getText());
                    pieceViews[i][j] = null;
                }
            }
        }
    }

    private void updateBoardView() {
        clearPieces();
        drawables.clear();
        drawPieces();
        highlightKingWhenCheck(ChessBoardView.king); 
    }

    private void handleTileClick(int x, int y) {
        Player currentPlayer = game.getCurrentPlayer();
        Piece clickedPiece = board.getPieceAt(x, y);

        if (selectedPosition == null) {
            if (clickedPiece != null && clickedPiece.getColor().equals(currentPlayer.getColor())) {
                selectedPosition = new Position(x, y);
                highlightValidMoves(clickedPiece.getValidMoves(board));
            }
        } else {
            Position from = selectedPosition;
            Position to = new Position(x, y);
            game.makeMove(from, to);
            selectedPosition = null;
            clearHighlights();
            updateBoardView();
        }
    }

    private void highlightValidMoves(List<Position> validMoves) {
        clearHighlights(); 

        for (Position pos : validMoves) {
            Rectangle tile = tiles[pos.getX()][pos.getY()];
            tile.setFill(Color.LIGHTGREEN);
            highlightedTiles.add(tile);
        }
    }

    private void highlightKingWhenCheck(King king) {
        clearHighlights(); 

        if (king != null && game.isInCheck(game.getCurrentPlayer())) {
            Position kingPosition = king.getPosition();
            int x = kingPosition.getX();
            int y = kingPosition.getY();
            Rectangle tile = tiles[x][y];
            tile.setFill(Color.LIGHTCORAL);  
            highlightedTiles.add(tile); 
        }
    }

    private void clearHighlights() {
        for (Rectangle tile : highlightedTiles) {
            int row = GridPane.getRowIndex(tile);
            int col = GridPane.getColumnIndex(tile);

            if ((row + col) % 2 == 0) {
                tile.setFill(Color.BEIGE);
            } else {
                tile.setFill(Color.BROWN);
            }
        }
        highlightedTiles.clear();
    }

    public King getKing() {
        return king;
    }

    public static void setCheckKing(King king) {
        ChessBoardView.king = king;
    }
}
