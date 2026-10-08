package com.lodge;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainApp extends Application {

    private TextField guestNameField = new TextField();
    private TextField roomRateField = new TextField();
    private DatePicker checkInDate = new DatePicker(LocalDate.now());
    private DatePicker checkOutDate = new DatePicker(LocalDate.now().plusDays(1));
    private ComboBox<String> checkInTime = new ComboBox<>();
    private ComboBox<String> checkOutTime = new ComboBox<>();
    private Label totalLabel = new Label("Total: ZMW 0.00");
    private Label nightsLabel = new Label("Nights: 1 (auto-calculated)");
    private TextArea receiptArea = new TextArea();

    @Override
    public void start(Stage stage) {
        // HEADER
        Label title = new Label("MULUNGUSHI LODGE");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);
        Label subtitle = new Label("Billing-Checking Management System ");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#CFF5FF"));
        VBox headerText = new VBox(2, title, subtitle);
        HBox header = new HBox(headerText);
        header.setPadding(new Insets(18, 25, 18, 25));
        header.setStyle("-fx-background-color: linear-gradient(to right, #0A4D68, #088395, #05BFDB);");

        // WATERMARK
        Label watermark = new Label("🏨");
        watermark.setStyle("-fx-font-size: 280px; -fx-opacity: 0.05;");
        StackPane watermarkPane = new StackPane(watermark);
        watermarkPane.setMouseTransparent(true);

        // Time options
        checkInTime.getItems().addAll("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM");
        checkOutTime.getItems().addAll("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM");
        checkInTime.setValue("02:00 PM");
        checkOutTime.setValue("11:00 AM");
        checkInTime.setStyle("-fx-background-radius: 8;");
        checkOutTime.setStyle("-fx-background-radius: 8;");
        checkInDate.setStyle("-fx-background-radius: 8;");
        checkOutDate.setStyle("-fx-background-radius: 8;");

        // FORM
        guestNameField.setPromptText("Enter guest full name");
        roomRateField.setPromptText("e.g. 600");
        styleInput(guestNameField);
        styleInput(roomRateField);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));
        form.add(createLabel("Guest Name:"), 0, 0);
        form.add(guestNameField, 1, 0, 3, 1);
        form.add(createLabel("Room Rate (ZMW):"), 0, 1);
        form.add(roomRateField, 1, 1, 3, 1);

        form.add(createLabel("Check-In:"), 0, 2);
        form.add(checkInDate, 1, 2);
        form.add(checkInTime, 2, 2);

        form.add(createLabel("Check-Out:"), 0, 3);
        form.add(checkOutDate, 1, 3);
        form.add(checkOutTime, 2, 3);

        form.add(nightsLabel, 1, 4, 3, 1);
        nightsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        nightsLabel.setTextFill(Color.web("#088395"));

        // Auto-calculate nights when dates change
        checkInDate.valueProperty().addListener((o, old, ne) -> updateNights());
        checkOutDate.valueProperty().addListener((o, old, ne) -> updateNights());

        // BUTTONS - Round with ocean colors
        Button calcBtn = createRoundButton("Calculate Bill", "#088395", "#0A4D68");
        Button clearBtn = createRoundButton("Clear", "#6C757D", "#495057");
        Button printBtn = createRoundButton("Print Receipt", "#05BFDB", "#088395");

        calcBtn.setOnAction(e -> calculateBill());
        clearBtn.setOnAction(e -> clearForm());
        printBtn.setOnAction(e -> printReceipt(stage));

        HBox buttons = new HBox(12, calcBtn, clearBtn, printBtn);
        buttons.setPadding(new Insets(0, 20, 10, 20));

        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        totalLabel.setTextFill(Color.web("#0A4D68"));
        totalLabel.setPadding(new Insets(5, 20, 5, 20));

        // RECEIPT
        receiptArea.setFont(Font.font("Consolas", 12));
        receiptArea.setPrefRowCount(14);
        receiptArea.setWrapText(true);
        receiptArea.setStyle(
                "-fx-control-inner-background: #F0FAFF; " +
                        "-fx-border-color: #05BFDB; -fx-border-width: 2; " +
                        "-fx-border-radius: 12; -fx-background-radius: 12; " +
                        "-fx-text-fill: #0A4D68;"
        );
        receiptArea.setText("Receipt will appear here...\n\n[Check-In/Out details will be shown]");

        VBox receiptBox = new VBox(6, new Label("Guest Receipt - Ocean Edition"), receiptArea);
        receiptBox.setPadding(new Insets(0, 20, 20, 20));
        VBox.setVgrow(receiptArea, Priority.ALWAYS);

        VBox content = new VBox(5, form, buttons, totalLabel, receiptBox);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 15, 0, 0, 5);");
        content.setMaxWidth(680);

        StackPane centerWrapper = new StackPane(watermarkPane, content);
        centerWrapper.setPadding(new Insets(25));
        centerWrapper.setStyle("-fx-background-color: #E8F6FA;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(centerWrapper);

        Scene scene = new Scene(root, 800, 750);
        stage.setTitle("Lodge Billing System - Check In/Out Edition");
        stage.setScene(scene);
        stage.show();
    }

    private void updateNights() {
        try {
            if (checkInDate.getValue() != null && checkOutDate.getValue() != null) {
                long nights = ChronoUnit.DAYS.between(checkInDate.getValue(), checkOutDate.getValue());
                if (nights <= 0) nights = 1;
                nightsLabel.setText("Nights: " + nights + " (auto-calculated)");
            }
        } catch (Exception ignored) {}
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setTextFill(Color.web("#0A4D68"));
        l.setMinWidth(130);
        return l;
    }

    private void styleInput(TextField tf) {
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: #B8E6F0; -fx-border-width: 1.5; -fx-padding: 8; -fx-font-size: 13;");
        tf.setPrefWidth(300);
    }

    private Button createRoundButton(String text, String color, String hoverColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 25; -fx-border-radius: 25; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-background-radius: 25; -fx-border-radius: 25; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 3);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 25; -fx-border-radius: 25; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
        return btn;
    }

    private void calculateBill() {
        try {
            String guest = guestNameField.getText().trim();
            double rate = Double.parseDouble(roomRateField.getText().trim());
            LocalDate inDate = checkInDate.getValue();
            LocalDate outDate = checkOutDate.getValue();
            String inTime = checkInTime.getValue();
            String outTime = checkOutTime.getValue();

            if (guest.isEmpty()) { showAlert("Enter guest name"); return; }
            if (inDate == null || outDate == null) { showAlert("Select Check-In and Check-Out dates"); return; }

            long nights = ChronoUnit.DAYS.between(inDate, outDate);
            if (nights <= 0) nights = 1;

            double total = rate * nights;
            totalLabel.setText(String.format("Total: ZMW %.2f for %d night(s)", total, nights));
            nightsLabel.setText("Nights: " + nights + " (auto-calculated)");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
            String receipt =
                    "╔══════════════════════════════════════════╗\n" +
                            "║      MULUNGUSHI LODGE - OCEAN VIEW        ║\n" +
                            "║       OFFICIAL GUEST RECEIPT             ║\n" +
                            "╠══════════════════════════════════════════╣\n" +
                            String.format("║ Guest Name : %-27s ║\n", guest.length()>27?guest.substring(0,27):guest) +
                            "╠══════════════════════════════════════════╣\n" +
                            String.format("║ Check-In  : %s at %-10s    ║\n", inDate.format(fmt), inTime) +
                            String.format("║ Check-Out : %s at %-10s    ║\n", outDate.format(fmt), outTime) +
                            "╠══════════════════════════════════════════╣\n" +
                            String.format("║ Nights    : %-3d nights                    ║\n", nights) +
                            String.format("║ Rate      : ZMW %-8.2f /night            ║\n", rate) +
                            "║                                          ║\n" +
                            String.format("║ TOTAL     : ZMW %-10.2f               ║\n", total) +
                            "╠══════════════════════════════════════════╣\n" +
                            "║ Thank you for staying with us!           ║\n" +
                            "║ Enjoy our Ocean Breeze Service           ║\n" +
                            "╚══════════════════════════════════════════╝\n\n" +
                            "Printed: " + LocalDate.now().format(fmt) + " at " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")) + "\n" +
                            "Location: Mulungushi Lodge, Zambia";

            receiptArea.setText(receipt);

        } catch (NumberFormatException ex) {
            showAlert("Enter valid number for Room Rate");
        }
    }

    private void clearForm() {
        guestNameField.clear();
        roomRateField.clear();
        checkInDate.setValue(LocalDate.now());
        checkOutDate.setValue(LocalDate.now().plusDays(1));
        checkInTime.setValue("02:00 PM");
        checkOutTime.setValue("11:00 AM");
        totalLabel.setText("Total: ZMW 0.00");
        nightsLabel.setText("Nights: 1 (auto-calculated)");
        receiptArea.setText("Receipt will appear here...");
    }

    private void printReceipt(Stage stage) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(stage)) {
            if (job.printPage(receiptArea)) job.endJob();
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    public static void main(String[] args) { launch(); }
}
