package hu.alkprojekt.model;

import javafx.scene.paint.Color;

// Background osztály megvalósítása.
public class Background extends Object {

    public Background(Direction direction, Color color) {
        super(direction, color);
    }

    @Override
    public void collideWith(Snake snake) {}
}
