package model;

public class Player {
    protected String name;
    protected String color; 

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
}
