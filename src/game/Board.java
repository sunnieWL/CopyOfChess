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
    private static final int BOARD_SIZE = 8;
    
    public Board(Boolean isGamble) {
        grid = new Piece[BOARD_SIZE][BOARD_SIZE];
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
        for (int i = 0; i < BOARD_SIZE; i++) {
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            grid[6][i] = new Pawn("white", new Position(6, i));
        }

        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                grid[i][j] = null;
            }
        }
    }
    
    private void initializeIdenticalPiecePattern() {
        List<Piece> blackPieces = createRandomPieceList("black");

        Collections.shuffle(blackPieces, random);
        Collections.shuffle(blackPieces, random);
        Collections.shuffle(blackPieces, random);
  
        List<Piece> whitePieces = new ArrayList<>();
        for (Piece piece : blackPieces) {
            Piece whitePiece = createPieceWithColor(piece, "white");
            whitePieces.add(whitePiece);
        }

        placePiecesOnRow(blackPieces, 0);
        placePiecesOnRow(whitePieces, 7);

        for (int i = 0; i < BOARD_SIZE; i++) {
            grid[1][i] = new Pawn("black", new Position(1, i));
            grid[6][i] = new Pawn("white", new Position(6, i));
        }

        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
    
    private void placePiecesOnRow(List<Piece> pieces, int row) {
        for (int i = 0; i < pieces.size(); i++) {
            int column = i;      
            grid[row][column] = pieces.get(i);
            pieces.get(i).setPosition(new Position(row, column));
        }
    }

    public void setBoardSetup(boolean isRandom) {
        if (isRandom) {
            initializeIdenticalPiecePattern();
        } else {
            initializeStandardPieces();
        }
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >=0 && x <BOARD_SIZE && y >=0 && y <BOARD_SIZE;
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
    
    public boolean isSquareUnderAttack(Position pos, String defendingColor, boolean checkKingMove) {
        String opponentColor = defendingColor.equals("white") ? "black" : "white";
        List<Piece> opponentPieces = getAllPieces(opponentColor);
        for (Piece piece : opponentPieces) {
            if (checkKingMove && piece instanceof King) {
                continue;
            }
            List<Position> moves = piece.getValidMoves(this);
            if (moves.contains(pos)) {
                return true;
            }
        }
        return false;
    }

}
