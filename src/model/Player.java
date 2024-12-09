package model;

import game.Board;
import game.Move;
import game.Game;

public class Player {
    protected String name;
    protected String color; 
    protected Game game;

    public Player(String name, String color, Game game) {
        this.name = name;
        this.color = color;
        this.game = game;
    }

    public String getName() { return name; }
    public String getColor() { return color; }

   
    //public Move makeMove(){};
}
