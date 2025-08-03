package io.github.zufarm.library.view.book;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.BookService;
import io.github.zufarm.library.services.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class BookDetailView {
    private final BookService bookService = new BookService();
    private final PersonService personService = new PersonService();
    private BookDTO book;
    private Stage detailStage;
    private Runnable onUpdate;
    private GridPane grid;
    
    public void showDetail(BookDTO book, Runnable onUpdate) {
        this.book = book;
        this.onUpdate = onUpdate;
        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Детали книги");

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        addBookInfo();
        
        updateAssignmentSection();
        
        addControlButtons();

        Scene scene = new Scene(grid);
        detailStage.setScene(scene);
        detailStage.show();
    }
    
    private void addBookInfo() {
        Label nameLabel = new Label(book.getName());
        Label authorLabel = new Label(book.getAuthor());
        Label yearLabel = new Label(String.valueOf(book.getYear()));

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorLabel, 1, 1);
        grid.add(new Label("Год:"), 0, 2);
        grid.add(yearLabel, 1, 2);
    }
    
    private void updateAssignmentSection() {
        grid.getChildren().removeIf(node -> 
            GridPane.getRowIndex(node) != null && 
            GridPane.getRowIndex(node) >= 3 && 
            GridPane.getRowIndex(node) <= 5
        );

        grid.add(new Label("Выдана:"), 0, 3);
        
        try {
            PersonDTO assignedPerson = personService.getBookHolder(book.getId());
            
            if (assignedPerson != null) {
                Label personLabel = new Label(assignedPerson.getFullName() + " (" + assignedPerson.getBirthYear() + ")");
                grid.add(personLabel, 1, 3);
                
                Button returnBtn = new Button("Вернуть книгу");
                returnBtn.setOnAction(e -> returnBook());
                grid.add(returnBtn, 1, 4);
            } else {
                ComboBox<PersonDTO> personCombo = new ComboBox<>();
                personCombo.setConverter(new StringConverter<PersonDTO>() {
                    @Override
                    public String toString(PersonDTO person) {
                        return person.getFullName() + " (" + person.getBirthYear() + ")";
                    }

                    @Override
                    public PersonDTO fromString(String string) {
                        return null;
                    }
                });
                
                ObservableList<PersonDTO> people = FXCollections.observableArrayList(personService.getAllPeople());
                personCombo.setItems(people);
                grid.add(personCombo, 1, 3);
                
                Button assignBtn = new Button("Выдать");
                assignBtn.setOnAction(e -> assignBook(personCombo.getSelectionModel().getSelectedItem()));
                grid.add(assignBtn, 1, 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grid.add(new Label("Ошибка загрузки данных"), 1, 3);
        }
    }
    
    private void returnBook() {
        if (bookService.returnBook(book.getId())) {
            updateAssignmentSection();
            onUpdate.run();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось вернуть книгу");
        }
    }
    
    private void assignBook(PersonDTO person) {
        if (person == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите человека из списка");
            return;
        }
        
        if (bookService.assignBook(book.getId(), person.getId())) {
            updateAssignmentSection();
            onUpdate.run();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось выдать книгу");
        }
    }
    
    private void addControlButtons() {
        HBox buttonBox = new HBox(10);
        
        Button editBtn = new Button("Редактировать");
        editBtn.setOnAction(e -> showEditForm());
        
        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> handleDelete());

        buttonBox.getChildren().addAll(editBtn, deleteBtn);
        grid.add(buttonBox, 1, 5);
    }
    
    private void showEditForm() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Редактировать книгу");

        GridPane editGrid = new GridPane();
        editGrid.setHgap(10);
        editGrid.setVgap(10);
        editGrid.setPadding(new Insets(20));

        TextField nameField = new TextField(book.getName());
        TextField authorField = new TextField(book.getAuthor());
        TextField yearField = new TextField(String.valueOf(book.getYear()));

        editGrid.add(new Label("Название:"), 0, 0);
        editGrid.add(nameField, 1, 0);
        editGrid.add(new Label("Автор:"), 0, 1);
        editGrid.add(authorField, 1, 1);
        editGrid.add(new Label("Год:"), 0, 2);
        editGrid.add(yearField, 1, 2);

        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> {
            try {
                book.setName(nameField.getText());
                book.setAuthor(authorField.getText());
                book.setYear(Integer.parseInt(yearField.getText()));
                
                if (bookService.updateBook(book)) {
                    onUpdate.run();
                    detailStage.close();
                    stage.close();
                    showDetail(book, onUpdate);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось обновить книгу");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Некорректный год издания");
            }
        });

        editGrid.add(saveBtn, 1, 3);
        stage.setScene(new Scene(editGrid));
        stage.show();
    }
    
    private void handleDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить эту книгу?");
        confirmation.setContentText(book.getName() + " - " + book.getAuthor());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bookService.deleteBook(book.getId())) {
                    onUpdate.run();
                    detailStage.close(); 
                } else {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось удалить книгу");
                }
            }
        });
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}