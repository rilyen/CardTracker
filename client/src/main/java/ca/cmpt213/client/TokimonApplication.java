package ca.cmpt213.client;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TokimonApplication extends Application {
    private VBox imageContainer = new VBox();

    @Override
    public void start(Stage stage) throws IOException {
        // Create tabs (each tab has a different functionality)
        TabPane tabPane = new TabPane();
        // Tab 1: Add Tokimon
        Tab addTokimonTab = new Tab("Add Tokimon");
        addTokimonTab.setContent(createAddTokimonContent());
        // Tab 2: Edit Tokimon (already in system)
        Tab editTokimonTab = new Tab("Edit Tokimon");
        editTokimonTab.setContent(createEditTokimonContent());
        // Tab 3: Delete Tokimon
        Tab deleteTokimonTab = new Tab("Delete Tokimon");
        deleteTokimonTab.setContent(createDeleteTokimonContent());
        // Tab 4: Get All Tokimon
        Tab getAllTokimonTab = new Tab("Get All Tokimon");
        getAllTokimonTab.setContent(wrapInScrollPane(createGetAllTokimonContent()));
        // Tab 5: Get Tokimon with id
        Tab getTokimonWithIdTab = new Tab("Get Tokimon with id");
        getTokimonWithIdTab.setContent(createGetTokimonWithIdContent());
        // Add tabs to TabPane
        tabPane.getTabs().addAll(addTokimonTab, editTokimonTab, deleteTokimonTab, getAllTokimonTab, getTokimonWithIdTab);


        // Set up Scene
        Scene scene = new Scene(tabPane, 600, 600);
        stage.setTitle("Tokimon Tracker Application");
        stage.setScene(scene);
        stage.show();
    }

    private ScrollPane wrapInScrollPane(VBox vbox) {
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createAddTokimonContent() {
        ComboBox<String> typeComboBox = new ComboBox<>();
        ComboBox<String> rarityScoreComboBox = new ComboBox<>();
        Label nameLabel = new Label("Tokimon name: ");
        Label typeLabel = new Label("Tokimon type: ");
        Label rarityLabel = new Label("Tokimon rarity score: ");
        TextField nameField = new TextField();
        Button submit = new Button("POST REQUEST ADD TOKIMON");
        List<String> types = Arrays.asList(
                "bug", "dragon", "electric", "fighting", "fire",
                "flying", "ghost", "grass", "ground", "ice",
                "normal", "poison", "psychic", "rock", "water",
                "unknown");
        typeComboBox.getItems().addAll(types);
        typeComboBox.setPromptText("Select Tokimon Type");
        List<String> rarityScores = Arrays.asList(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10");
        rarityScoreComboBox.getItems().addAll(rarityScores);
        rarityScoreComboBox.setPromptText("Select Tokimon Rarity Score");

        Label outputLabel = new Label();
        VBox imageContainer = new VBox();

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageContainer.getChildren().clear();
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                String rarityScore = rarityScoreComboBox.getValue();
                boolean hasName = !name.isEmpty();
                boolean hasType = (type != null);
                boolean hasRarityScore = (rarityScore != null);
                if (hasName && hasType && hasRarityScore) {
                    try {
                        URI uri = new URI("http://localhost:8080/tokimon");
                        URL url = uri.toURL();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/json");
                        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
//                        System.out.println("{\"name\":\"" + name + "\",\"type\":" + type + "\",\"rarityScore\":" + rarityScore + "}");
                        String tokimonString = "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"rarityScore\":" + rarityScore + "}";
                        wr.write(tokimonString);
                        wr.flush();
                        wr.close();
                        // connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 201) {  // 201 = HttpStatus.CREATED
                            // Parse into a Tokimon
                            Tokimon tokimon = ProcessInput.readInputToCard(tokimonString);
                            // Get the type image associated with this tokimon
                            Image img = new Image("http://localhost:8080/" + tokimon.getType() + ".png");
                            TokimonCard tokimonCard = new TokimonCard(tokimon, img);
                            VBox tokimonCardVBox = tokimonCard.createTokimonCardWithLabels();
                            tokimonCardVBox.getChildren().remove(2);
                            tokimonCardVBox.getChildren().remove(2);
                            imageContainer.getChildren().add(tokimonCardVBox);
                            outputLabel.setText("Tokimon added successfully");
//                        System.out.println("Tokimon added successfully");
                        } else {
                            System.out.println("Failed to add Tokimon. Response code is: " + responseCode);
                            outputLabel.setText("Failed to add Tokimon. Response code is: " + responseCode);
                        }
                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    outputLabel.setText("Unable to add Tokimon with missing features");
                }
            }
        });
        outputLabel.setText("output");

        GridPane gridpane = new GridPane();
        gridpane.add(nameLabel, 0, 0);
        gridpane.add(typeLabel, 0, 1);
        gridpane.add(rarityLabel, 0, 2);
        gridpane.add(nameField, 1, 0);
        gridpane.add(typeComboBox, 1, 1);
        gridpane.add(rarityScoreComboBox, 1, 2);
        gridpane.add(submit, 1, 3);

        return new VBox(20, gridpane, outputLabel, imageContainer);
    }

    private VBox createEditTokimonContent() {
        ComboBox<String> typeComboBox = new ComboBox<>();
        ComboBox<String> rarityScoreComboBox = new ComboBox<>();
        Label nameLabel = new Label("Tokimon name: ");
        Label typeLabel = new Label("Tokimon type: ");
        Label rarityLabel = new Label("Tokimon rarity score: ");
        Label idLabel = new Label("Tokimon id: ");
        TextField nameField = new TextField();
        Button submit = new Button("PUT REQUEST EDIT TOKIMON");
        List<String> types = Arrays.asList(
                "bug", "dragon", "electric", "fighting", "fire",
                "flying", "ghost", "grass", "ground", "ice",
                "normal", "poison", "psychic", "rock", "water",
                "unknown");
        typeComboBox.getItems().addAll(types);
        typeComboBox.setPromptText("Select Tokimon Type");
        List<String> rarityScores = Arrays.asList(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10");
        rarityScoreComboBox.getItems().addAll(rarityScores);
        rarityScoreComboBox.setPromptText("Select Tokimon Rarity Score");
        TextField idField = new TextField();

        Label outputLabel = new Label();
        VBox imageContainer = new VBox();

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageContainer.getChildren().clear();
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                String rarityScore = rarityScoreComboBox.getValue();
                String id = idField.getText();
//                System.out.println("name: " + name);
//                System.out.println(name.getClass().getName());
//                System.out.println("type: " + type);
//                System.out.println("rarityScore: " + rarityScore);
//                System.out.println("id: " + id);
                if (name.isEmpty()) {
                    name = "null";
                }
                if (type == "") {
                    type = "null";
                }
                if (rarityScore == null) {
                    rarityScore = "-1";
                }
                if (id == null) {
                    id = "-1";
                }
                try {
                    // Create JSON payload
                    String jsonInputString = String.format(
                            "{\"id\":%s,\"name\":\"%s\",\"type\":\"%s\",\"rarityScore\":%s}",
                            id, name, type, rarityScore
                    );
                    // Set up the HTTP connection
                    URI uri = new URI("http://localhost:8080/tokimon/edit/" + id);
                    URL url = uri.toURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    // Send the JSON payload
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        System.out.println("Tokimon updated successfully.");
                        //String tokimonString = {"name":"pidgey","type":"normal","rarityScore":3,"id":8}
                        String tokimonString = String.format(
                                "{\"name\":%s,\"type\":\"%s\",\"rarityScore\":\"%s\",\"id\":%s}",
                                name, type, rarityScore, id
                        );
                        // Parse into a Tokimon
                        Tokimon tokimon = ProcessInput.readInputToCard(tokimonString);
                        // Get the type image associated with this tokimon
                        Image img = new Image("http://localhost:8080/" + tokimon.getType() + ".png");
                        TokimonCard tokimonCard = new TokimonCard(tokimon,img);
                        VBox tokimonCardVBox = tokimonCard.createTokimonCardWithLabels();
                        tokimonCardVBox.getChildren().remove(2);
                        tokimonCardVBox.getChildren().remove(2);
                        imageContainer.getChildren().add(tokimonCardVBox);
                        outputLabel.setText(tokimonString + "\nTokimon updated successfully");
                        System.out.println("Tokimon added successfully");
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        Image img = new Image("http://localhost:8080/404_error.png");
                        ImageView imageView = new ImageView(img);
                        VBox unknownVBox = new VBox();
                        unknownVBox.getChildren().add(imageView);
                        imageContainer.getChildren().add(unknownVBox);
                        System.out.println("Could not find Tokimon. Response code: " + responseCode);
                        outputLabel.setText("Could not find Tokimon");
                    } else {
                        System.out.println("Unexpected response: " + responseCode);
                        outputLabel.setText("Unexpected response: " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // outputLabel.setText("output");

        GridPane gridpane = new GridPane();
        gridpane.add(nameLabel, 0, 0);
        gridpane.add(typeLabel, 0, 1);
        gridpane.add(rarityLabel, 0, 2);
        gridpane.add(idLabel, 0, 3);
        gridpane.add(nameField, 1, 0);
        gridpane.add(typeComboBox, 1, 1);
        gridpane.add(rarityScoreComboBox, 1, 2);
        gridpane.add(idField, 1, 3);
        gridpane.add(submit, 1, 4);

        return new VBox(20, gridpane, outputLabel, imageContainer);
    }

    private VBox createDeleteTokimonContent() {
        Label idLabel = new Label("Tokimon id: ");
        TextField idField = new TextField();
        Button submit = new Button("DELETE REQUEST");
        Label outputLabel = new Label();
        VBox imageContainer = new VBox();
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageContainer.getChildren().clear();
                String id = idField.getText();
                try {
                    URI uri = new URI("http://localhost:8080/tokimon/" + id);
                    URL url = uri.toURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                        System.out.println("Tokimon with id " + id + " deleted successfully.");
                        outputLabel.setText("Tokimon deleted successfully.");
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        outputLabel.setText("Could not find Tokimon");
                        Image img = new Image("http://localhost:8080/404_error.png");
                        ImageView imageView = new ImageView(img);
                        VBox unknownVBox = new VBox();
                        unknownVBox.getChildren().add(imageView);
                        imageContainer.getChildren().add(unknownVBox);

                    } else {
                        System.out.println("Failed to delete Tokimon. Response code is: " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.add(idLabel, 0, 0);
        gridpane.add(idField, 1, 0);
        gridpane.add(submit, 1, 1);
        return new VBox(20, gridpane, outputLabel, imageContainer);
    }

    private VBox createGetAllTokimonContent() {
        Button submit = new Button("GET ALL TOKIMON");
        Label outputLabel = new Label();
        // Create a GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridPane.getChildren().clear();
                Gson gson = new Gson();
                try {
                    URI uri = new URI("http://localhost:8080/tokimon");
                    URL url = uri.toURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));  // The response from the server
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {  // should be one line
                            response.append(line);
                        }
                        br.close();
                        String responseBody = response.toString();
                        System.out.println(responseBody);
                        // Deserialize the JSON to get the tokimonList array
                        JsonElement jsonElement = JsonParser.parseString(responseBody);
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        // Iterate through the array
                        int row = 0;
                        int col = 0;
                        for (JsonElement element : jsonArray) {
                            // get the Tokimon object
                            Tokimon tokimon = gson.fromJson(element, Tokimon.class);
                            // create the image and add to the gridpane
                            Image img = new Image("http://localhost:8080/" + tokimon.getType() + ".png");
                            TokimonCard tokimonCard = new TokimonCard(tokimon, img);
                            VBox tokimonCardVBox = tokimonCard.createTokimonCardWithLabels();
                            gridPane.add(tokimonCardVBox, col, row);
                            col++;
                            if (col >= 5) {
                                col = 0;
                                row++;
                            }
                        }
//                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
//                        Type tokimonListType = new TypeToken<List<Tokimon>>(){}.getType();
//                        List<Tokimon> tokimonList = gson.fromJson(jsonObject.get("tokimonListType"), tokimonListType);
//                        for (int i=0; i<tokimonList.size(); i++) {
//                            Tokimon tokimon = tokimonList.get(i);
//                            // Get the type image associated with this tokimon
//                            Image img = new Image("http://localhost:8080/" + tokimon.getType() + ".png");
//                            TokimonCard tokimonCard = new TokimonCard(tokimon, img);
//                            VBox tokimonCardVBox = tokimonCard.createTokimonCardWithLabels();
//                            gridPane.getChildren().add(tokimonCardVBox);
//                        }
                        System.out.println("Tokimon fetched successfully.");
                    } else {
                        System.out.println("Failed to fetch all Tokimon. Reponse code is: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // Create a VBox and add the submit button at the top with the gridpane underneath
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(submit, gridPane);
        return vbox;
        // gridPane.add(submit);
        //return new VBox(20, gridpane, outputLabel);
        //return gridPane;
    }

    private VBox createGetTokimonWithIdContent() {
        Label idLabel = new Label("Tokimon id: ");
        TextField idField = new TextField();
        Button submit = new Button("GET TOKIMON WITH ID REQUEST");
        Label outputLabel = new Label();
        VBox imageContainer = new VBox();

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageContainer.getChildren().clear();
                String id = idField.getText();
                try{
                    URI uri = new URI("http://localhost:8080/tokimon/" + id);
                    URL url = uri.toURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream())
                        );
                        String output = br.readLine();
                        System.out.println(output);
                        System.out.println(connection.getResponseCode());
                        // Parse the Json Object into a Tokimon
                        Tokimon tokimon = ProcessInput.readInputToCard(output);
                        // Get the type image associated with this tokimon
                        Image img = new Image("http://localhost:8080/" + tokimon.getType() + ".png");
                        TokimonCard tokimonCard = new TokimonCard(tokimon, img);
                        VBox tokimonCardVBox = tokimonCard.createTokimonCardWithLabels();
                        // imageContainer.getChildren().clear();
                        imageContainer.getChildren().add(tokimonCardVBox);
                        outputLabel.setText(output);
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        Image img = new Image("http://localhost:8080/404_error.png");
                        ImageView imageView = new ImageView(img);
                        VBox unknownVBox = new VBox();
                        unknownVBox.getChildren().add(imageView);
                        imageContainer.getChildren().add(unknownVBox);
                        System.out.println("Could not find Tokimon. Response code: " + responseCode);
                        outputLabel.setText("Could not find Tokimon");
                    } else {
                        System.out.println("Unexpected response code: " + responseCode);
                        outputLabel.setText("Unexpected response code: " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        GridPane gridpane = new GridPane();
        gridpane.add(idLabel, 0, 0);
        gridpane.add(idField, 1, 0);
        gridpane.add(submit, 1, 1);
        return new VBox(20, gridpane, outputLabel, imageContainer);
    }

    public static void main(String[] args) {
        launch();
    }
}