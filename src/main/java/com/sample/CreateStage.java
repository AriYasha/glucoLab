package com.sample;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class CreateStage {

    private TextField fileName;
    private TextField comment;

    public CreateStage(String fName) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Сохранение...");
        dialog.setHeaderText("Можно добавить комментарий и изменить название");

// Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
        ButtonType saveButtonType = new ButtonType("Сахранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        fileName = new TextField();
        fileName.setPromptText("Имя файла");
        fileName.setText(fName);
        comment = new TextField();
        comment.setPromptText("Комментарий");

        grid.add(new Label("Имя файла:"), 0, 0);
        grid.add(fileName, 1, 0);
        grid.add(new Label("Комментарий:"), 0, 1);
        grid.add(comment, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(saveButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        fileName.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> fileName.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(fileName.getText(), comment.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
    }

    public String getFileName() {
        return fileName.getText();
    }

    public String getComment() {
        return fileName.getText();
    }
}
