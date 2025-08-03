package io.github.zufarm.library.view.person;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.BookService;
import io.github.zufarm.library.services.PersonService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PersonDetailView {
    private final PersonService personService = new PersonService();
    private PersonDTO person;
    private Stage detailStage;
    
    public void showDetail(PersonDTO person, Runnable onUpdate) {
        this.person = person;
        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Детали читателя");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label nameLabel = new Label(person.getFullName());
        Label yearLabel = new Label(String.valueOf(person.getBirthYear()));

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new Label("Год рождения:"), 0, 2);
        grid.add(yearLabel, 1, 2);

        HBox buttonBox = new HBox(10);
        
        Button editBtn = new Button("Редактировать");
        editBtn.setOnAction(e -> showEditForm(detailStage, onUpdate));
        
        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> handleDelete(onUpdate));

        buttonBox.getChildren().addAll(editBtn, deleteBtn);
        grid.add(buttonBox, 1, 3);

        Scene scene = new Scene(grid);
        detailStage.setScene(scene);
        detailStage.show();
    }
    
    private void handleDelete(Runnable onUpdate) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить этого читателя?");
        confirmation.setContentText(person.getFullName());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (personService.deletePerson(person.getId())) {
                    onUpdate.run();
                    detailStage.close(); 
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при удалении читателя").show();
                }
            }
        });
    }

    private void showEditForm(Stage parentStage, Runnable onUpdate) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Редактировать читателя");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(person.getFullName());
        TextField yearField = new TextField(String.valueOf(person.getBirthYear()));

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Год рождения:"), 0, 2);
        grid.add(yearField, 1, 2);

        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> {
            try {
         
                person.setFullName(nameField.getText());
                person.setBirthYear(Integer.parseInt(yearField.getText()));     
                
                if (personService.updatePerson(person)) {
                    onUpdate.run();
                    parentStage.close();
                    stage.close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при обновлении человека").show();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Ошибка: " + ex.getMessage()).show();
            }
        });

        grid.add(saveBtn, 1, 3);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}