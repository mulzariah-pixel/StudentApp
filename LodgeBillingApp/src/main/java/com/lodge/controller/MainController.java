package com.lodge.controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
    @FXML private TextField guestNameField, roomRateField, nightsField;
    @FXML private Label totalLabel;
    @FXML private TextArea receiptArea;
    private double lastTotal = 0;

    @FXML public void calculateBill() {
        try {
            double rate = Double.parseDouble(roomRateField.getText());
            int nights = Integer.parseInt(nightsField.getText());
            lastTotal = rate * nights;
            totalLabel.setText(String.format("Total: ZMW %.2f", lastTotal));
            receiptArea.setText("GUEST: " + guestNameField.getText() + "\nRATE: ZMW " + rate + " x " + nights + " nights\nTOTAL: ZMW " + lastTotal + "\n\n--- Thank you for staying with us ---");
        } catch (Exception e) { totalLabel.setText("Error: Enter valid numbers"); }
    }
    @FXML public void clearFields() { guestNameField.clear(); roomRateField.setText("500"); nightsField.setText("1"); totalLabel.setText("Total: ZMW 0.00"); receiptArea.clear(); }
    @FXML public void printReceipt() { receiptArea.setText(receiptArea.getText() + "\n\n[Printed at Mufulira Lodge]"); }
}