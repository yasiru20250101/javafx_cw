package com.example.courswork.util;

import com.example.courswork.model.part;
import com.example.courswork.model.cart;
import com.example.courswork.service.InventoryService;
import com.example.courswork.service.DealerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TableView<part> tableParts;

    @FXML
    private TableColumn<part, String> colPartId;
    @FXML
    private TableColumn<part, String> colPartName;
    @FXML
    private TableColumn<part, String> colBrand;
    @FXML
    private TableColumn<part, Double> colPrice;
    @FXML
    private TableColumn<part, Integer> colQty;
    @FXML
    private TableColumn<part, String> colCategory;
    @FXML
    private TableColumn<part, String> colDate;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private TextField minPriceField;
    @FXML
    private TextField maxPriceField;

    @FXML
    private Label totalCountLabel;
    @FXML
    private Label totalValueLabel;
    @FXML
    private Label lowStockLabel;

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

    private InventoryService inventoryService;
    private DealerService dealerService;
    private cart cart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String inventoryPath = "src/main/resources/com/example/courswork/inventory_legacy.txt";
        String dealersPath = "src/main/resources/com/example/courswork/dealers_legacy.txt";
        String auditPath = "src/main/resources/com/example/courswork/audit_log.txt";

        inventoryService = new InventoryService(inventoryPath, auditPath, 5);
        dealerService = new DealerService(dealersPath);
        cart = new cart();

        inventoryService.loadParts();
        dealerService.loadDealers();

        setupTableColumns();
        setupCategoryFilter();

        refreshTable();
        refreshLowStock();

        System.out.println("Loaded " + inventoryService.getTotalCount() + " parts");
        System.out.println("Loaded " + dealerService.getTotalDealers() + " dealers");
    }

    public void refreshLowStock() {
        List<part> lowStock = inventoryService.getLowStockParts();

        if (lowStock.isEmpty()) {
            lowStockLabel.setText("All parts are sufficiently stocked.");
        } else {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < lowStock.size(); i++) {
                part p = lowStock.get(i);
                message.append(p.getPartid()).append(" - ").append(p.getPartname())
                        .append(" (Qty: ").append(p.getQuantity()).append(")");
                if (i < lowStock.size() - 1) {
                    message.append(",  ");
                }
            }
            lowStockLabel.setText(message.toString());
        }
    }

    public void setupCategoryFilter() {
        categoryFilter.getItems().add("All Categories");
        categoryFilter.getItems().add("engine");
        categoryFilter.getItems().add("electrical");
        categoryFilter.getItems().add("brakes");
        categoryFilter.getItems().add("bodywork");
        categoryFilter.setValue("All Categories");
    }

    @FXML
    public void onResetClicked() {
        searchField.clear();
        categoryFilter.setValue("All Categories");
        minPriceField.clear();
        maxPriceField.clear();
        refreshTable();
    }

    @FXML
    public void onSearchClicked() {
        String keyword = searchField.getText().trim();
        String category = categoryFilter.getValue();
        if (category == null || category.equals("All Categories")) {
            category = "";
        }

        double minPrice = 0;
        double maxPrice = 0;
        try {
            if (!minPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            }
            if (!maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showAlert("Price filters must be valid numbers");
            return;
        }

        List<part> results = inventoryService.searchParts(keyword, category, minPrice, maxPrice);
        tableParts.setItems(FXCollections.observableArrayList(results));
    }

    public void refreshTable() {
        List<part> sorted = inventoryService.getSortedParts();
        ObservableList<part> observableList = FXCollections.observableArrayList(sorted);
        tableParts.setItems(observableList);

        totalCountLabel.setText("Total Parts: " + inventoryService.getTotalCount());
        totalValueLabel.setText("Total Value: Rs. " + String.format("%.2f", inventoryService.getTotalValue()));
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tuk Tuk Spare Parts");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setupTableColumns() {
        colPartId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPartid()));

        colPartName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPartname()));

        colBrand.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBrand()));

        colPrice.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        colQty.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());

        colCategory.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDateAdded()));
    }

    @FXML
    public void onAddClick() {
        showPartDialog(null);
    }

    @FXML
    public void onUpdateClick() {
        part selected = tableParts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a part from the table first.");
            return;
        }
        showPartDialog(selected);
    }

    @FXML
    public void onDeleteClick() {
        part selected = tableParts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a part from the table first.");
            return;
        }
        inventoryService.deletePart(selected.getPartid());
        refreshTable();
        refreshLowStock();
        showAlert("Part " + selected.getPartid() + " deleted successfully.");
    }

    @FXML
    public void onClearClick() {
        txtPartId.clear();
        txtPartName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQuantity.clear();
        txtCategory.clear();
        txtDate.clear();
        txtImage.clear();
        tableParts.getSelectionModel().clearSelection();
    }

    public void showPartDialog(part existingPart) {
        Dialog<part> dialog = new Dialog<>();

        if (existingPart == null) {
            dialog.setTitle("Add new Part");
            dialog.setHeaderText("Enter details for the new part");
        } else {
            dialog.setTitle("Edit Part");
            dialog.setHeaderText("Enter details for " + existingPart.getPartid());
        }

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField partIdField = new TextField();
        partIdField.setPromptText("eg: P012");

        TextField partNameField = new TextField();
        partNameField.setPromptText("eg: Bajaj Brake Pad");

        TextField brandField = new TextField();
        brandField.setPromptText("eg: Bajaj");

        TextField priceField = new TextField();
        priceField.setPromptText("eg: 1500.00");

        TextField quantityField = new TextField();
        quantityField.setPromptText("eg: 10");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("engine", "electrical", "brakes", "bodywork");
        categoryBox.setPromptText("Select category: ");

        TextField dateField = new TextField();
        dateField.setPromptText("eg: 15-09-2009");

        TextField imageField = new TextField();
        imageField.setPromptText("eg: image.jpg");

        if (existingPart != null) {
            partIdField.setText(existingPart.getPartid());
            partIdField.setEditable(false);
            partNameField.setText(existingPart.getPartname());
            brandField.setText(existingPart.getBrand());
            priceField.setText(String.valueOf(existingPart.getPrice()));
            quantityField.setText(String.valueOf(existingPart.getQuantity()));
            categoryBox.setValue(existingPart.getCategory());
            dateField.setText(existingPart.getDateAdded());
            imageField.setText(existingPart.getImage());
        }

        grid.add(new Label("Part ID:"), 0, 0);
        grid.add(partIdField, 1, 0);
        grid.add(new Label("Part Name:"), 0, 1);
        grid.add(partNameField, 1, 1);
        grid.add(new Label("Brand:"), 0, 2);
        grid.add(brandField, 1, 2);
        grid.add(new Label("Price (Rs.):"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantityField, 1, 4);
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryBox, 1, 5);
        grid.add(new Label("Date Added:"), 0, 6);
        grid.add(dateField, 1, 6);
        grid.add(new Label("Image Path:"), 0, 7);
        grid.add(imageField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String partId = partIdField.getText().trim();
                String partName = partNameField.getText().trim();
                String brand = brandField.getText().trim();
                String priceText = priceField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String category = categoryBox.getValue();
                String dateAdded = dateField.getText().trim();
                String imagePath = imageField.getText().trim();

                if (partId.isEmpty() || partName.isEmpty() || priceText.isEmpty()
                        || quantityText.isEmpty() || category == null) {
                    showAlert("Part ID, Name, Price, Quantity and Category is required");
                    return null;
                }

                double price;
                int quantity;

                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    showAlert("Price must be valid number ");
                    return null;
                }

                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    showAlert("Quantity must be a valid number ");
                    return null;
                }
                if (price <= 0) {
                    showAlert("Price must be greater than zero ");
                    return null;
                }
                if (quantity < 0) {
                    showAlert("Quantity cannot be negative ");
                    return null;
                }
                return new part(partId, partName, brand, price, quantity,
                        category.toLowerCase(), dateAdded, imagePath);
            }
            return null;
        });

        Optional<part> result = dialog.showAndWait();

        result.ifPresent(savedPart -> {
            if (existingPart == null) {
                inventoryService.addPart(savedPart);
            } else {
                inventoryService.updatePart(savedPart.getPartid(), savedPart);
            }
            refreshTable();
            refreshLowStock();
        });
    }
}
