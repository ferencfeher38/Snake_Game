package hu.alkprojekt.controller;

import hu.alkprojekt.App;
import hu.alkprojekt.dao.LeaderboardDAO;
import hu.alkprojekt.dao.LeaderboardDAOImpl;
import hu.alkprojekt.model.Object;
import hu.alkprojekt.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class gameController implements Initializable {

    public static int height;
    public static int width;

    @FXML
    private Label playerTwo;
    @FXML
    private Label playerOne;
    @FXML
    private TextField name;
    @FXML
    private TextField name2;
    @FXML
    private Label labelScore2;
    @FXML
    private TextField textFieldHighScore2;
    @FXML
    private TextField textFieldHighScore;
    @FXML
    private ComboBox<String> comboBoxMode;
    @FXML
    private Label labelScore;
    @FXML
    private Pane paneOptions;
    @FXML
    public Pane paneGame;
    @FXML
    public Pane paneGameOver;
    @FXML
    private ComboBox<String> comboBoxGridSize;
    @FXML
    private ComboBox<String> comboBoxWall;
    @FXML
    private ComboBox<Integer> comboBoxNumberOfFruit;
    @FXML
    private ColorPicker comboBoxFruitColor;
    @FXML
    private ComboBox<String> comboBoxSnakeSpeed;
    @FXML
    private ColorPicker comboBoxSnakeColor;

    public LeaderboardDAO dao = new LeaderboardDAOImpl();

    // Név és score mentése.
    public void saveData() throws IOException {

        if(comboBoxMode.getValue().equals("Egy játékos")) {
            Leaderboard l = new Leaderboard();
            l.setName(name.getText());
            l.setScore(App.score);
            l.setId(-1);
            dao.save(l);
        } else {
            Leaderboard l = new Leaderboard();
            l.setName(name.getText());
            l.setScore(App.score);
            l.setId(-1);
            dao.save(l);

            Leaderboard lb = new Leaderboard();
            lb.setName(name2.getText());
            lb.setScore(App.score2);
            lb.setId(0);
            dao.save(lb);
        }
        App.setRoot("game");
    }

    // Kilépés.
    @FXML
    public void exit() {
        Platform.exit();
    }

    // Átlépés a toplistába.
    @FXML
    private void switchToToplist() throws IOException {
        App.setRoot("toplist");
    }

    // Játék indítása gomb.
    public void buttonStartGamePressed() {
        paneOptions.setVisible(false);
        paneGame.setVisible(true);
        startGame();
    }

    // Váltás az  opciókra gomb.
    public void buttonChangeOptionsPressed() {
        paneGameOver.setVisible(false);
        paneOptions.setVisible(true);
    }

    // Játék újra gomb.
    public void buttonPlayAgainPressed() {
        paneGameOver.setVisible(false);
        paneGame.setVisible(true);
        startGame();
    }

    // Objectek kirajzolása.
    public void drawObject(Object o) {
        Rectangle tmp = new Rectangle(20, 20, o.getColor());
        tmp.setLayoutX(20 * o.getDirection().x);
        tmp.setLayoutY(20 * o.getDirection().y);
        paneGame.getChildren().addAll(tmp);

    }

    // Snake részein végigiterálás amik szintén objectek.
    public void drawSnake(Snake snake) {
        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.UP);
        directions.add(Direction.DOWN);
        directions.add(Direction.RIGHT);
        directions.add(Direction.LEFT);
        Object tail = snake.getBody().get(snake.getBody().size()-1);
        for (int i = 0; i < 4; i++) {
            Direction tailTmp = tail.getDirection().sum(directions.get(i));
            Object tmp = new Background(new Direction(tailTmp), Color.WHITE);
            drawObject(tmp);
        }
        for(Object o : snake.getBody()) {
            drawObject(o);
        }
    }

    // Border rajzolása.
    public void drawBorder(Border border) {
        for(Wall wall : border.getWalls()) {
            drawObject(wall);
        }
    }

    // Background rajzolása.
    public void drawBackground(int width, int height) {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                drawObject(new Background(new Direction(i, j), Color.WHITE));
            }
        }
    }

    // Ütközések  kezelése.
    public void collisonHandler(Border border, Snake snake, ArrayList<Fruit> fruits) {
        for(Wall wall : border.getWalls()) {
            wall.collideWith(snake);
        }
        for(Fruit fruit : fruits) {
            fruit.collideWith(snake);
        }
        snake.collideWithSelf();
    }

    Direction direction  = Direction.RIGHT;
    // Ez a második snake-nek.
    Direction direction2  = Direction.LEFT;
    public void startGame() {
        // Játékoszám csekk, beállítás.
        if(comboBoxMode.getValue().equals("Egy játékos")) {

            // Pálya csekk, beállítás.
            if(comboBoxGridSize.getValue().equals("Kicsi")) {
                height = 500;
                width =  600;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            } else if(comboBoxGridSize.getValue().equals("Normál")) {
                height = 600;
                width = 700;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            } else {
                height = 700;
                width  = 800;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            }
            int scale = 20;
            Direction size = new Direction(width / scale, height /  scale);

            drawBackground(size.x, size.y);

            // Speed csekk, beállítás.
            int speed = 100000000;
            if(comboBoxSnakeSpeed.getValue().equals("Lassú")) {
                speed = 150000000;
            } else if(comboBoxSnakeSpeed.getValue().equals("Gyors")) {
                speed = 50000000;
            }

            // Objectek létrehozása.
            Border border = new Border(size.x, size.y, Color.BLACK);
            ArrayList<Snake> snakes = new ArrayList<>();
            Snake snake = new Snake(new Direction(5,5), comboBoxSnakeColor.getValue());
            snakes.add(snake);
            ArrayList<Fruit> fruits = new ArrayList<>();
            for (int i = 0; i < comboBoxNumberOfFruit.getValue(); i++) {
                fruits.add(new Fruit(comboBoxFruitColor.getValue(), size.x, size.y));
            }


            int finalSpeed = speed;

            // AnimationTimer létrehozása.
            AnimationTimer animationTimer = new AnimationTimer() {
                private long lastUpdate = 0;
                @Override
                public void handle(long l) {
                    if (l - lastUpdate >= finalSpeed) {
                        lastUpdate = l ;

                        // Snake/-kek lekezelése.
                        for (Snake snake : snakes) {
                            snake.moveSnake(direction);
                            collisonHandler(border, snake, fruits);
                            if(snake.isDead()) {
                                stop();
                                quitGame(snakes);
                                direction = Direction.RIGHT;
                            }
                            drawSnake(snake);
                            drawBorder(border);
                            for(Fruit fruit : fruits) {
                                drawObject(fruit);
                            }
                        }
                    }
                }
            };

            animationTimer.start();

            // Irányok lekezelése.
            paneGame.requestFocus();
            paneGame.setOnKeyPressed(event -> {
                if(event.getCode() == KeyCode.UP) {
                    direction = Direction.UP;
                } else if(event.getCode() == KeyCode.DOWN) {
                    direction = Direction.DOWN;
                } else if(event.getCode() == KeyCode.RIGHT) {
                    direction = Direction.RIGHT;
                } else if(event.getCode() == KeyCode.LEFT) {
                    direction = Direction.LEFT;
                }
            });

        } else {

            // Többjátékos mód.
            // Pálya csekk, beállítás.
            if(comboBoxGridSize.getValue().equals("Kicsi")) {
                height = 500;
                width =  600;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            } else if(comboBoxGridSize.getValue().equals("Normál")) {
                height = 600;
                width = 700;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            } else {
                height = 700;
                width  = 800;
                paneGame.setPrefHeight(height);
                paneGame.setPrefWidth(width);
            }
            int scale = 20;
            Direction size = new Direction(width / scale, height /  scale);

            drawBackground(size.x, size.y);

            // Speed csekk, beállítás.
            int speed = 100000000;
            if(comboBoxSnakeSpeed.getValue().equals("Lassú")) {
                speed = 150000000;
            } else if(comboBoxSnakeSpeed.getValue().equals("Gyors")) {
                speed = 50000000;
            }

            // Objectek létrehozása.
            Border border = new Border(size.x, size.y, Color.BLACK);
            ArrayList<Snake> snakes = new ArrayList<>();
            Snake snake = new Snake(new Direction(5,5), comboBoxSnakeColor.getValue());
            Snake snake2 = new Snake(new Direction(10,10), comboBoxSnakeColor.getValue());
            snakes.add(snake);
            snakes.add(snake2);
            ArrayList<Fruit> fruits = new ArrayList<>();
            for (int i = 0; i < comboBoxNumberOfFruit.getValue(); i++) {
                fruits.add(new Fruit(comboBoxFruitColor.getValue(), size.x, size.y));
            }


            int finalSpeed = speed;

            // AnimationTimer létrehozása.
            AnimationTimer animationTimer = new AnimationTimer() {
                private long lastUpdate = 0;
                @Override
                public void handle(long l) {
                    if (l - lastUpdate >= finalSpeed) {
                        lastUpdate = l ;

                        // Snakek lekezelése.
                        snakes.get(0).moveSnake(direction);
                        snakes.get(1).moveSnake(direction2);

                        for (Snake snake : snakes) {
                            collisonHandler(border, snake, fruits);
                            if(snake.isDead()) {
                                stop();
                                quitGame(snakes);
                                direction = Direction.RIGHT;
                                direction2 = Direction.RIGHT;
                            }
                            drawSnake(snake);
                            drawBorder(border);
                            for(Fruit fruit : fruits) {
                                drawObject(fruit);
                            }
                        }

                        /*
                        // Snakek lekezelése, itt még alakítani kell elegánsabbra.
                        for (Snake snake : snakes) {
                            snake.moveSnake(direction);
                            collisonHandler(border, snake, fruits);
                            if(snake.isDead()) {
                                stop();
                                quitGame(snakes);
                                direction = Direction.RIGHT;
                            }
                            drawSnake(snake);
                            drawBorder(border);
                            for(Fruit fruit : fruits) {
                                drawObject(fruit);
                            }
                        }
                         */
                    }
                }
            };

            animationTimer.start();

            // Irányok lekezelése két snakenél.
            paneGame.requestFocus();
            paneGame.setOnKeyPressed(event -> {
                if(!snake.isDead()) {
                    if (event.getCode() == KeyCode.UP) {
                        direction = Direction.UP;
                    } else if (event.getCode() == KeyCode.DOWN) {
                        direction = Direction.DOWN;
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        direction = Direction.RIGHT;
                    } else if (event.getCode() == KeyCode.LEFT) {
                        direction = Direction.LEFT;
                    }
                }
                if(!snake2.isDead()) {
                    if (event.getCode() == KeyCode.W) {
                        direction2 = Direction.UP;
                    } else if (event.getCode() == KeyCode.S) {
                        direction2 = Direction.DOWN;
                    } else if (event.getCode() == KeyCode.D) {
                        direction2 = Direction.RIGHT;
                    } else if (event.getCode() == KeyCode.A) {
                        direction2 = Direction.LEFT;
                    }
                }
             });
        }
    }

    int score;
    int score2;
    private void quitGame(ArrayList<Snake> snakes) {
        for(Snake snake : snakes) {
            paneGame.setVisible(false);
            if(snakes.size() == 1) {
                paneGameOver.setVisible(true);
                playerOne.setText("Játékos neve");
                playerTwo.setVisible(false);
                labelScore.setText("Elért pontszám: " + (snakes.get(0).getBody().size()-3));
                labelScore2.setVisible(false);
                name2.setVisible(false);
                textFieldHighScore2.setVisible(false);
                textFieldHighScore.setText(Integer.toString(snakes.get(0).getBody().size()-3));
                App.score = snakes.get(0).getBody().size()-3;
            } else {
                playerTwo.setVisible(true);
                paneGameOver.setVisible(true);
                playerOne.setText("Snake1 játékos neve");
                labelScore.setText("Snake1 elért pontszám: " + (snakes.get(0).getBody().size()-3));
                labelScore2.setText("Snake2 elért pontszám: " + (snakes.get(1).getBody().size()-3));
                labelScore2.setVisible(true);
                name2.setVisible(true);
                textFieldHighScore.setText(Integer.toString(snakes.get(0).getBody().size()-3));
                textFieldHighScore2.setText(Integer.toString(snakes.get(1).getBody().size()-3));
                textFieldHighScore2.setVisible(true);
                App.score = snakes.get(0).getBody().size()-3;
                App.score2 = snakes.get(1).getBody().size()-3;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneGame.setVisible(false);
        paneOptions.setVisible(true);
        paneGameOver.setVisible(false);

        comboBoxMode.getItems().addAll("Egy játékos", "Két játékos");
        comboBoxMode.setValue("Egy játékos");

        comboBoxGridSize.getItems().addAll("Kicsi", "Normál", "Nagy");
        comboBoxGridSize.setValue("Normál");

        comboBoxFruitColor.setValue(Color.rgb(255,0,0));
        comboBoxSnakeColor.setValue(Color.rgb(0,128,0));

        comboBoxSnakeSpeed.getItems().addAll("Lassú", "Normál", "Gyors");
        comboBoxSnakeSpeed.setValue("Normál");

        comboBoxNumberOfFruit.getItems().addAll(1, 3, 5);
        comboBoxNumberOfFruit.setValue(1);

        comboBoxWall.getItems().addAll("Fallal körülvett", "Falak nélkül");
        comboBoxWall.setValue("Fallal körülvett");
    }
}
