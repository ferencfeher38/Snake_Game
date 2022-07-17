package hu.alkprojekt.model;

import javafx.scene.paint.Color;

// Fal  osztály megvalósítása.
public class Wall extends Object {

    public Wall(Direction direction, Color color) {
        super(direction, color);
    }

    @Override
    public void collideWith(Snake snake) {
        if(getDirection().isEquals(snake.getBody().get(0).getDirection())) {
            snake.setDead(true);
        }
    }
}
