package hu.alkprojekt.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

// Snake osztály megvalósítása.
public class Snake {
    private final ArrayList<BodyPart> body;
    private Direction currentDirection = Direction.RIGHT;
    private boolean isDead =  false;

    public boolean isDead() {
        return isDead;
    }

    public Snake(Direction direction, Color color) {
        this.body = new ArrayList<>();
        body.add(new BodyPart(direction, color));
        body.add(new BodyPart(new Direction(direction.x - 1, direction.y), color));
        body.add(new BodyPart(new Direction(direction.x - 2, direction.y), color));
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public ArrayList<BodyPart> getBody() {
        return body;
    }

    // Snake mozgás.
    public void moveSnake(Direction direction) {
        if(!currentDirection.isEquals(direction)) {
            if(direction.equals(Direction.UP) && !currentDirection.isEquals(Direction.DOWN)) {
                currentDirection = Direction.UP;
            } else if(direction.equals(Direction.DOWN) && !currentDirection.isEquals(Direction.UP)) {
                currentDirection = Direction.DOWN;
            } else if(direction.equals(Direction.RIGHT) && !currentDirection.isEquals(Direction.LEFT)) {
                currentDirection = Direction.RIGHT;
            } else if(direction.equals(Direction.LEFT) && !currentDirection.isEquals(Direction.RIGHT)) {
                currentDirection = Direction.LEFT;
            }
        }
        ArrayList<Direction> bodyTmp = new ArrayList<>();
        for(Object o : body) {
            bodyTmp.add(new Direction(o.getDirection()));
        }
        for(int i = 1; i < body.size(); i++) {
            body.get(i).setDirection(bodyTmp.get(i-1));
        }
        Direction tmp = body.get(0).getDirection();
        body.get(0).setDirection(new Direction(tmp.sum(currentDirection)));
    }

    // Saját magábaütközik.
    public void collideWithSelf() {
        for(int i = 1; i < body.size(); i++) {
            if(body.get(i).getDirection().isEquals(body.get(0).getDirection())) {
                isDead = true;
            }
        }
    }
}