package game;

import pieces.Piece;
import pieces.King;
import pieces.Queen;
import pieces.Rook;
import pieces.Bishop;
import pieces.Knight;
import pieces.Pawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Position;

public class Board {
    private Piece[][] grid;
    private Random random;
    private Boolean isGamble;
    
    public Board(Boolean isGamble) {
        grid = new Piece[8][8];
        random = new Random();
        this.isGamble  = isGamble;
        setBoardSetup(isGamble);
    }

    private void initializeStandardPieces() {
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
    
//    public void initializeRandomPieces() {
//        // Randomly shuffle pieces for black (row 0) and white (row 7)
//        List<Piece> blackPieces = createRandomPieceList("black");
//        List<Piece> whitePieces = createRandomPieceList("white");
//
//        // Shuffle and place random pieces for both black and white
//        Collections.shuffle(blackPieces);
//        Collections.shuffle(whitePieces);
//
//        // Place shuffled pieces in row 0 (black) and row 7 (white)
//        placeRandomPiecesOnRow(blackPieces, 0);
//        placeRandomPiecesOnRow(whitePieces, 7);
//
//        // Pawns are always in the second (1) and second last (6) rows
//        for (int i = 0; i < 8; i++) {
//            grid[1][i] = new Pawn("black", new Position(1, i));
//            grid[6][i] = new Pawn("white", new Position(6, i));
//        }
//
//        // Clear rows 2 to 5 as they are empty in chess
//        for (int i = 2; i <= 5; i++) {
//            for (int j = 0; j < 8; j++) {
//                grid[i][j] = null;
//            }
//        }
//    }
    
    private void initializeIdenticalPiecePattern() {
        // Generate the shuffled pieces for black
        List<Piece> blackPieces = createRandomPieceList("black");

        // Shuffle the black pieces list once
        Collections.shuffle(blackPieces, random);
        Collections.shuffle(blackPieces, random);
        Collections.shuffle(blackPieces, random);

        // Create the same pattern for white by changing the color to white
        List<Piece> whitePieces = new ArrayList<>();
        for (Piece piece : blackPieces) {
            // Create a new piece of the same type and change its color to white
            Piece whitePiece = createPieceWithColor(piece, "white");
            whitePieces.add(whitePiece);
        }

        // Place the shuffled black pieces on row 0
        placePiecesOnRow(blackPieces, 0);

        // Place the shuffled white pieces on row 7 (with reversed color)
        placePiecesOnRow(whitePieces, 7);

        // Pawns are always in the second (1) and second last (6) rows
        for (int i = 0; i < 8; i++) {
            grid[1][i] = new Pawn("black", new Position(1, i));
            grid[6][i] = new Pawn("white", new Position(6, i));
        }

        // Clear rows 2 to 5 as they are empty in chess
        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = null;
            }
        }
    }
    
    private Piece createPieceWithColor(Piece original, String newColor) {
        if (original instanceof Rook) {
            return new Rook(newColor, original.getPosition());
        } else if (original instanceof Knight) {
            return new Knight(newColor, original.getPosition());
        } else if (original instanceof Bishop) {
            return new Bishop(newColor, original.getPosition());
        } else if (original instanceof Queen) {
            return new Queen(newColor, original.getPosition());
        } else if (original instanceof King) {
            return new King(newColor, original.getPosition());
        } else if (original instanceof Pawn) {
            return new Pawn(newColor, original.getPosition());
        } else {
            throw new IllegalArgumentException("Unknown piece type");
        }
    }
    
    // Helper method to create the list of pieces (2 Knights, 2 Bishops, 2 Rooks, 1 Queen, 1 King)
    private List<Piece> createRandomPieceList(String color) {
        List<Piece> pieces = new ArrayList<>();
        pieces.add(new Rook(color, null));
        pieces.add(new Rook(color, null));
        pieces.add(new Knight(color, null));
        pieces.add(new Knight(color, null));
        pieces.add(new Bishop(color, null));
        pieces.add(new Bishop(color, null));
        pieces.add(new Queen(color, null));
        pieces.add(new King(color, null));
        return pieces;
    }
    
 // Helper method to place the pieces on a given row
    private void placePiecesOnRow(List<Piece> pieces, int row) {
        for (int i = 0; i < pieces.size(); i++) {
            int column;
            // Randomly choose a column and place the piece
            do {
                column = i;
            } while (grid[row][column] != null); // Ensure the spot is not occupied
            grid[row][column] = pieces.get(i);
            pieces.get(i).setPosition(new Position(row, column));
        }
    }

    // Helper method to place the random pieces on a given row
    private void placeRandomPiecesOnRow(List<Piece> pieces, int row) {
        for (int i = 0; i < pieces.size(); i++) {
            int column;
            // Randomly choose a column and place the piece
            do {
                column = random.nextInt(8);
            } while (grid[row][column] != null); // Ensure the spot is not occupied
            grid[row][column] = pieces.get(i);
            pieces.get(i).setPosition(new Position(row, column));
        }
    }

    // Method to switch between standard and random setup
    public void setBoardSetup(boolean isRandom) {
        if (isRandom) {
            initializeIdenticalPiecePattern();
        } else {
            initializeStandardPieces();
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
    public void setPieceAt(Position position, Piece piece) {
        grid[position.getX()][position.getY()] = piece;
    }
    
    public void removePiece(Position pos) {
        if (isValidPosition(pos.getX(), pos.getY())) {
            grid[pos.getX()][pos.getY()] = null;
        }
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
        Board newBoard = new Board(isGamble);
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
    
    public boolean isSquareUnderAttack(Position pos, String defendingColor) {
        String opponentColor = defendingColor.equals("white") ? "black" : "white";
        List<Piece> opponentPieces = getAllPieces(opponentColor);
        for (Piece piece : opponentPieces) {
            List<Position> moves = piece.getValidMoves(this);
            if (moves.contains(pos)) {
                return true;
            }
        }
        return false;
    }

}
