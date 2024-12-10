package gui;

import interfaces.Drawable;
import pieces.Piece;
import javafx.scene.text.Text;

public abstract class PieceView implements Drawable {
    private Piece piece;
    private Text text;

    public PieceView(Piece piece) {
        this.piece = piece;
        this.text = new Text(piece.getSymbol());
        this.text.setStyle("-fx-font-size: 36;");
    }

    public Piece getPiece() {
        return piece;
    }

    public Text getText() {
        return text;
    }
    
    @Override
    public void update() {
        text.setText(piece.getSymbol());
    }
}
