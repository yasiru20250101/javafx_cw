package com.example.courswork.util;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField txtPartId;

    @FXML
    private TextField txtPartName;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtImage;

    @FXML
    private TableView<?> tableParts;

    @FXML
    public void initialize() {
        System.out.println("GUI Loaded Successfully");
    }
}