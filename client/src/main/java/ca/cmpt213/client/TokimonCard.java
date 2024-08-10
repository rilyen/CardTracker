package ca.cmpt213.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class TokimonCard {
    private Tokimon tokimon;
    private ImageView imageView;

    public TokimonCard(Tokimon tokimon, Image image) {
        this.tokimon = tokimon;
        this.imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
    }

    public VBox createTokimonCardWithLabels() {
        // Initial elements
        Label nameLabel = new Label("Name: " + tokimon.getName());
        // Additional elements (hidden initially)
        Label typeLabel = new Label("Type: " + tokimon.getType());
        Label rarityScoreLabel = new Label("Score: " + tokimon.getRarityScore());
        Label idLabel = new Label("Id: " + tokimon.getId());
        VBox additionalInfo = new VBox(typeLabel, rarityScoreLabel, idLabel);
        additionalInfo.setVisible(false);
        // Create 'More Info' button
        Button moreInfo = new Button("More Info");
        moreInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                additionalInfo.setVisible(!additionalInfo.isVisible());
                moreInfo.setText(additionalInfo.isVisible() ? "Less Info" : "More Info");
            }
        });
        VBox wholeCard = new VBox();
        wholeCard.setSpacing(10);
        wholeCard.getChildren().addAll(imageView, nameLabel, moreInfo, additionalInfo);
        return wholeCard;
    }
}
