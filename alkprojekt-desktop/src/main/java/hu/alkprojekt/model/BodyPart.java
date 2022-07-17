package hu.alkprojekt.model;

import javafx.scene.paint.Color;

// Bodypart osztály megvalósítása.
public class BodyPart extends Object {

    public BodyPart(Direction direction, Color color) {
        super(direction, color);
    }

    @Override
    public void collideWith(Snake snake) {
        if(getDirection().isEquals(snake.getBody().get(0).getDirection())) {
            snake.setDead(true);
        }
    }
}
