package ca.cmpt213.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TokimonController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}