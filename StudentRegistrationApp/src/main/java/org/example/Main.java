package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.repository.StudentRepository;
import org.example.service.StudentService;

public class Main extends Application {

    private final StudentRepository repository = new StudentRepository();
    private final StudentService service = new StudentService(repository);

    @Override
    public void start(Stage stage) {

        // Title
        Label title = new Label("STUDENT REGISTRATION");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Input fields
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Enter Student ID");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter Last Name");

        TextField programField = new TextField();
        programField.setPromptText("Enter Program");

        // Register button
        Button registerButton = new Button("REGISTER STUDENT");

        // Status message
        Label statusLabel = new Label();

        // Layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Student ID:"), 0, 0);
        form.add(studentIdField, 1, 0);

        form.add(new Label("First Name:"), 0, 1);
        form.add(firstNameField, 1, 1);

        form.add(new Label("Last Name:"), 0, 2);
        form.add(lastNameField, 1, 2);

        form.add(new Label("Program:"), 0, 3);
        form.add(programField, 1, 3);

        // Button action
        registerButton.setOnAction(event -> {

            String result = service.registerStudent(
                    studentIdField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    programField.getText()
            );

            statusLabel.setText(result);

            if (result.startsWith("SUCCESS")) {

                statusLabel.setStyle("-fx-text-fill: green;");

                studentIdField.clear();
                firstNameField.clear();
                lastNameField.clear();
                programField.clear();

            } else {

                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        VBox root = new VBox(
                15,
                title,
                form,
                registerButton,
                statusLabel
        );

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("Student Registration System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}