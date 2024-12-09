package game;

import pieces.Piece;
import pieces.King;
import pieces.Queen;
import pieces.Rook;
import pieces.Bishop;
import pieces.Knight;
import pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

import model.Position;

public class Board {
    private Piece[][] grid;

    public Board() {
        grid = new Piece[8][8];
        initializePieces();
    }

    private void initializePieces() {
        grid[0][0] = new Rook("black", new Position(0, 0));
        grid[0][1] = new Knight("black", new Position(0, 1));
        grid[0][2] = new Bishop("black", new Position(0, 2));
        grid[0][3] = new Queen("black", new Position(0, 3));
        grid[0][4] = new King("black", new Position(0, 4));
        grid[0][5] = new Bishop("black", new Position(0, 5));
        grid[0][6] = new Knight("black", new Position(0, 6));
        grid[0][7] = new Rook("black", new Position(0, 7));
        for (int i = 0; i < 8; i++) {
            grid[1][i] = new Pawn("black", new Position(1, i));
        }

        grid[7][0] = new Rook("white", new Position(7, 0));
        grid[7][1] = new Knight("white", new Position(7, 1));
        grid[7][2] = new Bishop("white", new Position(7, 2));
        grid[7][3] = new Queen("white", new Position(7, 3));
        grid[7][4] = new King("white", new Position(7, 4));
        grid[7][5] = new Bishop("white", new Position(7, 5));
        grid[7][6] = new Knight("white", new Position(7, 6));
        grid[7][7] = new Rook("white", new Position(7, 7));
        for (int i = 0; i < 8; i++) {
            grid[6][i] = new Pawn("white", new Position(6, i));
        }

        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = null;
            }
        }
    }

    public boolean isValidPosition(int x, int y) {
        return x >=0 && x <8 && y >=0 && y <8;
    }

    public Piece getPieceAt(int x, int y) {
        if (!isValidPosition(x, y)) return null;
        return grid[x][y];
    }

    public Piece getPieceAt(Position position) {
        return getPieceAt(position.getX(), position.getY());
    }

    public boolean movePiece(Position from, Position to) {
        if (!isValidPosition(from.getX(), from.getY()) || !isValidPosition(to.getX(), to.getY())) {
            return false;
        }
        Piece movingPiece = getPieceAt(from);
        if (movingPiece == null) {
            return false;
        }
        if (!movingPiece.getValidMoves(this).contains(to)) {
            return false;
        }
       
        grid[to.getX()][to.getY()] = movingPiece;
        grid[from.getX()][from.getY()] = null;
        movingPiece.move(to);
        return true;
    }
    
    public King findKing(String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = grid[i][j];
                if (piece instanceof King && piece.getColor().equals(color)) {
                    return (King) piece;
                }
            }
        }
        return null; 
    }
    
    public List<Piece> getAllPieces(String color) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = grid[i][j];
                if (piece != null && piece.getColor().equals(color)) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
    
    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = this.grid[i][j];
                if (piece != null) {
                    newBoard.grid[i][j] = piece.clone(); // Implement clone in Piece
                } else {
                    newBoard.grid[i][j] = null;
                }
            }
        }
        return newBoard;
    }
//    // For debug
//    public void display() {
//        for (int i =0; i <8; i++) {
//            for (int j =0; j <8; j++) {
//                Piece piece = grid[i][j];
//                if (piece != null) {
//                    System.out.print(piece.getSymbol() + " ");
//                } else {
//                    System.out.print("-- ");
//                }
//            }
//            System.out.println();
//        }
//    }
}
