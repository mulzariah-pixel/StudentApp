package zm.ac.mu.ict261.studentapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import zm.ac.mu.ict261.studentapp.model.Customer;

public class App extends Application {

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // =====================================================
        // HEADER
        // =====================================================

        Label appTitle = new Label("CUSTOMER ENTRY");
        appTitle.getStyleClass().add("app-title");

        Label appSubtitle =
                new Label("Customer Management System");
        appSubtitle.getStyleClass().add("app-subtitle");

        VBox headerText = new VBox(3);
        headerText.getChildren().addAll(
                appTitle,
                appSubtitle
        );

        HBox header = new HBox(headerText);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);


        // =====================================================
        // CUSTOMER FORM
        // =====================================================

        Label formTitle = new Label("Customer Details");
        formTitle.getStyleClass().add("section-title");

        // Name
        Label nameLabel = new Label("Customer Name");
        nameLabel.getStyleClass().add("form-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");
        nameField.getStyleClass().add("text-field");
        nameField.setPrefWidth(350);

        // Province
        Label provinceLabel = new Label("Province");
        provinceLabel.getStyleClass().add("form-label");

        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Select",
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceBox.setPromptText("Select province");
        provinceBox.getStyleClass().add("combo-box");
        provinceBox.setPrefWidth(350);


        // =====================================================
        // BUTTONS
        // =====================================================

        Button saveButton = new Button("Save Customer");
        saveButton.getStyleClass().add("primary-button");

        Button deleteButton = new Button("Delete Customer");
        deleteButton.getStyleClass().add("delete-button");


        HBox buttons = new HBox(12);
        buttons.getChildren().addAll(
                saveButton,
                deleteButton
        );
        buttons.setAlignment(Pos.CENTER_LEFT);


        // =====================================================
        // FORM GRID
        // =====================================================

        GridPane formGrid = new GridPane();

        formGrid.setHgap(15);
        formGrid.setVgap(10);

        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);

        formGrid.add(provinceLabel, 0, 1);
        formGrid.add(provinceBox, 1, 1);

        formGrid.add(buttons, 1, 2);


        VBox formCard = new VBox(18);

        formCard.getStyleClass().add("form-card");

        formCard.getChildren().addAll(
                formTitle,
                formGrid
        );


        // =====================================================
        // STATUS
        // =====================================================

        Label status = new Label();
        status.getStyleClass().add("status-label");


        // =====================================================
        // TABLE
        // =====================================================

        Label tableTitle = new Label("Customers");
        tableTitle.getStyleClass().add("section-title");

        TableView<Customer> table = new TableView<>();

        // Name column
        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        nameColumn.setPrefWidth(300);


        // Province column
        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        provinceColumn.setPrefWidth(250);


        table.getColumns().addAll(
                nameColumn,
                provinceColumn
        );

        table.setItems(customers);

        table.setPrefHeight(250);


        // =====================================================
        // SAVE CUSTOMER
        // =====================================================

        saveButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            // Validate name
            if (name.isEmpty()) {

                status.setText(
                        "Enter the customer name."
                );

                nameField.requestFocus();
                return;
            }

            // Validate province
            if (province == null) {

                status.setText(
                        "Choose a province."
                );

                provinceBox.requestFocus();
                return;
            }

            // Add customer
            customers.add(
                    new Customer(name, province)
            );

            // Success message
            status.setText(
                    "Customer saved successfully."
            );

            // Clear after successful save
            nameField.clear();

            provinceBox
                    .getSelectionModel()
                    .clearSelection();

            nameField.requestFocus();
        });


        // =====================================================
        // DELETE CUSTOMER
        // =====================================================

        deleteButton.setOnAction(event -> {

            Customer selected =
                    table.getSelectionModel()
                            .getSelectedItem();

            // No selection
            if (selected == null) {

                status.setText(
                        "Select a customer first."
                );

                return;
            }


            // Confirmation dialog
            ButtonType delete =
                    new ButtonType("Delete");

            Alert ask = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Delete the selected customer?",
                    delete,
                    ButtonType.CANCEL
            );

            ask.setTitle("Confirm Deletion");
            ask.setHeaderText(
                    "Confirm customer deletion"
            );


            // Only delete if confirmed
            if (ask.showAndWait()
                    .orElse(ButtonType.CANCEL)
                    == delete) {

                customers.remove(selected);

                status.setText(
                        "Customer deleted successfully."
                );
            }
        });


        // =====================================================
        // KEYBOARD ACCESS
        // =====================================================

        nameField.setOnAction(
                event -> saveButton.fire()
        );

        provinceBox.setOnAction(
                event -> saveButton.fire()
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox content = new VBox(15);

        content.setPadding(
                new Insets(25)
        );

        content.getChildren().addAll(
                formCard,
                status,
                tableTitle,
                table
        );


        // =====================================================
        // FOOTER
        // =====================================================

        Label footer = new Label(
                "ICT261 Advanced Java • Customer Manager"
        );

        footer.getStyleClass().add("footer");

        HBox footerBox = new HBox(footer);

        footerBox.setPadding(
                new Insets(10, 25, 15, 25)
        );

        footerBox.setAlignment(
                Pos.CENTER_RIGHT
        );


        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        BorderPane root = new BorderPane();

        root.setTop(header);
        root.setCenter(content);
        root.setBottom(footerBox);


        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(root, 800, 650);


        // Load CSS
        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );


        stage.setTitle(
                "CustomerEntry- Customer Manager"
        );

        stage.setScene(scene);

        stage.setMinWidth(700);
        stage.setMinHeight(600);

        stage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}