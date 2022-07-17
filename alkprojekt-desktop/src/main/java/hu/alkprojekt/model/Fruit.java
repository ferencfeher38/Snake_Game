package hu.alkprojekt.model;

import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// Gyümölcs osztály megvalósítása.
public class Fruit extends Object {

    private final int width;
    private final int height;
    private static final Random rand = new Random();

    public Fruit(Color color, int width, int height) {
        super(new Direction(0,0), color);
        this.width = width;
        this.height = height;
        moveFruit();
    }

    // Gyümölcsök random pozícinonálása.
    public void moveFruit() {
        int x = ThreadLocalRandom.current().nextInt(1, width - 1);
        int y = ThreadLocalRandom.current().nextInt(1, height - 1);
        setDirection(new Direction(x, y));
    }

    // Snakkel való ütközés.
    @Override
    public void collideWith(Snake snake) {
        if(getDirection().isEquals(snake.getBody().get(0).getDirection())) {
            snake.getBody().add(new BodyPart(new Direction(0,0), snake.getBody().get(0).getColor()));
            moveFruit();
        }
    }
}
