package hu.alkprojekt.model;

import javafx.scene.paint.Color;
import java.util.ArrayList;

// Border osztály megvalósítása.
public class Border {
    private final ArrayList<Wall> walls = new ArrayList<>();

    public Border(int width, int height, Color color) {
        for(int i = 0; i < width; i++) {
            walls.add(new Wall(new Direction(i, 0),color));
        }
        for(int i = 0; i < width; i++) {
            walls.add(new Wall(new Direction(i, height),color));
        }
        for(int i = 0; i < height; i++) {
            walls.add(new Wall(new Direction(0, i),color));
        }
        for(int i = 0; i < height; i++) {
            walls.add(new Wall(new Direction(width, i),color));
        }
        walls.add(new Wall(new Direction(width, height), color));
        walls.add(new Wall(new Direction(-1, 0), Color.GREY));
        walls.add(new Wall(new Direction(0, -1), color.GREY));
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }
}
