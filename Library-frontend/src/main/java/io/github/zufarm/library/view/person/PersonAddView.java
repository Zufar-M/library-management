package io.github.zufarm.library.view.person;

import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.PersonService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PersonAddView {
    private final PersonService personService = new PersonService();

    public void showForm(Runnable onSuccess) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Добавить читателя");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField yearField = new TextField();

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Год рождения:"), 0, 2);
        grid.add(yearField, 1, 2);

        Button submitBtn = new Button("Сохранить");
        submitBtn.setOnAction(e -> {
            try {
                PersonDTO person = new PersonDTO(0, nameField.getText(),Integer.parseInt(yearField.getText()));

                personService.addPerson(person);
                onSuccess.run();
                stage.close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Ошибка: " + ex.getMessage()).show();
            }
        });

        grid.add(submitBtn, 1, 3);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}