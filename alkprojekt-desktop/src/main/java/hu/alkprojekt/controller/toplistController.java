package hu.alkprojekt.controller;

import hu.alkprojekt.App;
import hu.alkprojekt.dao.LeaderboardDAO;
import hu.alkprojekt.dao.LeaderboardDAOImpl;
import hu.alkprojekt.model.Leaderboard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class toplistController implements Initializable {

    LeaderboardDAO dao = new LeaderboardDAOImpl();
    List<Leaderboard> leaderboardlist = dao.findAll();

    @FXML
    private VBox vBoxList;

    // Átlépés a mainbe.
    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("main");
    }

    // Kilépés
    @FXML
    public void exit() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Leaderboard leaderboard : leaderboardlist) {
            Label labelData = new Label("Név: " + leaderboard.getName() + " " + " Pontszám: " + leaderboard.getScore());
            labelData.setStyle("-fx-font-size: 30");
            vBoxList.getChildren().addAll(labelData);
        }
    }
}
