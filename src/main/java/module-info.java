module com.example.zpo_lab3_server {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zpo_lab3_server to javafx.fxml;
    exports com.example.zpo_lab3_server;
}