package com.example.courswork.util;

import com.example.courswork.model.part;
import com.example.courswork.model.cart;
import com.example.courswork.service.InventoryService;
import com.example.courswork.service.DealerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML private TextField txtPartId;
    @FXML private TextField txtPartName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtCategory;
    @FXML private TextField txtDate;
    @FXML private TextField txtImage;

    // Table view linkage
    @FXML private TableView<part> tableParts;

    // Table column linkages
    @FXML private TableColumn<part, String> colPartId;
    @FXML private TableColumn<part, String> colPartName;
    @FXML private TableColumn<part, String> colBrand;
    @FXML private TableColumn<part, Double> colPrice;
    @FXML private TableColumn<part, Integer> colQty;
    @FXML private TableColumn<part, String> colCategory;

    // Observable list to store inventory data dynamically
    private final ObservableList<part> partList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map TableView columns to the variable names in your part model class
        colPartId.setCellValueFactory(new PropertyValueFactory<>("partId"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Bind the list data directly into the UI table view
        tableParts.setItems(partList);
    }

    @FXML
    public void onAddClick() {
        try {
            // 1. Retrieve data from ALL 8 input fields
            String id = txtPartId.getText();
            String name = txtPartName.getText();
            String brand = txtBrand.getText();
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQuantity.getText());
            String category = txtCategory.getText();
            String date = txtDate.getText();
            String image = txtImage.getText();

            // 2. Pass ALL 8 arguments into the constructor to fix the error!
            part newPart = new part(id, name, brand, price, qty, category, date, image);
            partList.add(newPart);

            // Clean inputs for next entry
            clearFields();

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for Price and Quantity.");
        }
    }

    @FXML
    public void onClearClick() {
        clearFields();
    }

    private void clearFields() {
        txtPartId.clear();
        txtPartName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQuantity.clear();
        txtCategory.clear();
        txtDate.clear();
        txtImage.clear();
    }
}