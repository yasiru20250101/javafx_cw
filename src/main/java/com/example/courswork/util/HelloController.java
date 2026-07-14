package com.example.courswork.util;

import com.example.courswork.model.part;
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
    private TableView<part> tableParts;

    @FXML
    public void initialize() {
        System.out.println("GUI Loaded Successfully");
    }
}