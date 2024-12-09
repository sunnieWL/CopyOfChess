package model;

public class Position {
    private int x; 
    private int y; 

    public Position(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            throw new IllegalArgumentException("Invalid board position");
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        return equals((Position) obj);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
