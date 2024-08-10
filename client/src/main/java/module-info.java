module ca.cmpt213.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ca.cmpt213.client to javafx.fxml, com.google.gson;
    exports ca.cmpt213.client;
}