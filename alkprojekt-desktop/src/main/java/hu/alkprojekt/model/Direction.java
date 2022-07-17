package hu.alkprojekt.model;

// Direction osztály megvalósítása.
public class Direction {
    public int x;
    public int y;
    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Direction(Direction direction) {
        this.x = direction.x;
        this.y = direction.y;
    }


    public final static Direction UP = new Direction(0, -1);
    public final static Direction DOWN = new Direction(0, 1);
    public final static Direction RIGHT = new Direction(1, 0);
    public final static Direction LEFT = new Direction(-1, 0);

    public boolean isEquals(Direction direction) {
        return direction.x == x && direction.y == y;
    }

    public Direction sum(Direction direction) {
        return new Direction(x + direction.x, y + direction.y);
    }

}
