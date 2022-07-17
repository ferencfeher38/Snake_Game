package hu.alkprojekt.controller;

import hu.alkprojekt.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    // Átlépés az opciókba.
    @FXML
    private void switchToOptions() throws IOException {
        App.setRoot("game");
    }

    // Átlépés a toplistába.
    @FXML
    private void switchToToplist() throws IOException {
        App.setRoot("toplist");
    }

    // Kilépés
    @FXML
    public void exit() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
