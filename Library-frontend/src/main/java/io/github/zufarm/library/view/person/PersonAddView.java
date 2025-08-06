package io.github.zufarm.library.view.person;

import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.PersonService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PersonAddView {
    private final PersonService personService = new PersonService();

    public void showForm(Runnable onSuccess) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Добавить читателя");
        stage.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("admin-form");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label titleLabel = new Label("Добавить нового читателя");
        titleLabel.getStyleClass().add("admin-form-title");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label nameLabel = new Label("ФИО:");
        nameLabel.getStyleClass().add("admin-form-label");
        TextField nameField = new TextField();
        nameField.getStyleClass().add("admin-field");
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);

        Label birthDateLabel = new Label("Дата рождения (дд.мм.гггг):");
        birthDateLabel.getStyleClass().add("admin-form-label");
        TextField birthDateField = new TextField();
        birthDateField.getStyleClass().add("admin-field");
        birthDateField.setPromptText("дд.мм.гггг");
        grid.add(birthDateLabel, 0, 2);
        grid.add(birthDateField, 1, 2);

        Label phoneLabel = new Label("Номер телефона:");
        phoneLabel.getStyleClass().add("admin-form-label");
        TextField phoneField = new TextField();
        phoneField.getStyleClass().add("admin-field");
        phoneField.setPromptText("+7 XXX XXX-XX-XX");
        grid.add(phoneLabel, 0, 3);
        grid.add(phoneField, 1, 3);

        Button submitBtn = new Button("Сохранить");
        submitBtn.getStyleClass().addAll("admin-button", "admin-register-button");
        submitBtn.setOnAction(e -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate birthDate = LocalDate.parse(birthDateField.getText(), formatter);
                
                PersonDTO person = new PersonDTO(
                    0, 
                    nameField.getText(), 
                    birthDate, 
                    phoneField.getText()
                );
                
                personService.addPerson(person);
                onSuccess.run();
                stage.close();
            } catch (DateTimeParseException ex) {
                showAlert("Ошибка", "Некорректный формат даты. Используйте дд.мм.гггг");
            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage());
            }
        });

        grid.add(submitBtn, 1, 4);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.showAndWait();
    }
}