module com.example.courswork {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.courswork.util to javafx.fxml;

    exports com.example.courswork.util;
}