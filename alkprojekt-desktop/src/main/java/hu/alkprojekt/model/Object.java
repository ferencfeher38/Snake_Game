package hu.alkprojekt.model;

import javafx.scene.paint.Color;

// Object osztály létrehozása.
public abstract class Object {
    private final Color color;
    private Direction direction;


    public Object(Direction direction, Color color) {
        this.color = color;
        this.direction = direction;
    }

    public abstract void collideWith(Snake snake);

    public Color getColor() {
        return color;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
