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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.VBox;

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
        detailStage.setMinWidth(400);
        detailStage.setMinHeight(350);

        grid = new GridPane();
        grid.getStyleClass().add("admin-form");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(100);
        col1.setPrefWidth(100);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(200);
        col2.setPrefWidth(200);
        col2.setHgrow(Priority.ALWAYS);
        
        grid.getColumnConstraints().addAll(col1, col2);

        addBookInfo();
        updateAssignmentSection();
        addControlButtons();

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        detailStage.setScene(scene);
        detailStage.show();
    }
    
    private void addBookInfo() {
        Label titleLabel = new Label("Информация о книге");
        titleLabel.getStyleClass().add("admin-form-title");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label nameLabel = new Label(book.getName());
        nameLabel.getStyleClass().add("book-detail-label");
        
        Label authorLabel = new Label(book.getAuthor());
        authorLabel.getStyleClass().add("book-detail-label");
        
        Label yearLabel = new Label(String.valueOf(book.getYear()));
        yearLabel.getStyleClass().add("book-detail-label");

        grid.add(new Label("Название:"), 0, 1);
        grid.add(nameLabel, 1, 1);
        grid.add(new Label("Автор:"), 0, 2);
        grid.add(authorLabel, 1, 2);
        grid.add(new Label("Год:"), 0, 3);
        grid.add(yearLabel, 1, 3);
    }
    
    private void updateAssignmentSection() {
        double currentWidth = detailStage.getWidth();

        grid.getChildren().removeIf(node -> 
            GridPane.getRowIndex(node) != null && 
            GridPane.getRowIndex(node) >= 4 && 
            GridPane.getRowIndex(node) <= 6
        );

        Label assignmentLabel = new Label("Выдана:");
        assignmentLabel.getStyleClass().add("admin-form-label");
        grid.add(assignmentLabel, 0, 4);
        
        try {
            PersonDTO assignedPerson = personService.getBookHolder(book.getId());
            
            if (assignedPerson != null) {
                Label personLabel = new Label(assignedPerson.getFullName() + " (" + assignedPerson.getBirthYear() + ")");
                personLabel.getStyleClass().add("book-detail-label");
                grid.add(personLabel, 1, 4);
                
                Button returnBtn = new Button("Вернуть книгу");
                returnBtn.getStyleClass().addAll("button", "book-action-button");
                returnBtn.setOnAction(e -> returnBook());
                grid.add(returnBtn, 1, 5);
            } else {
                
                VBox searchContainer = new VBox(5);
                
                
                TextField searchField = new TextField();
                searchField.setPromptText("Поиск...");
                searchField.getStyleClass().add("admin-field");
                
                
                ComboBox<PersonDTO> personCombo = new ComboBox<>();
                personCombo.getStyleClass().add("admin-field");
                personCombo.setConverter(new StringConverter<PersonDTO>() {
                    @Override
                    public String toString(PersonDTO person) {
                        return person != null ? person.getFullName() + " (" + person.getBirthYear() + ")" : "Не выдана";
                    }

                    @Override
                    public PersonDTO fromString(String string) {
                        return null;
                    }
                });
                
               
                ObservableList<PersonDTO> allPeople = FXCollections.observableArrayList(personService.getAllPeople());
                FilteredList<PersonDTO> filteredPeople = new FilteredList<>(allPeople, p -> true);
                
               
                searchField.textProperty().addListener((obs, oldValue, newValue) -> {
                    filteredPeople.setPredicate(person -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        
                        String lowerCaseFilter = newValue.toLowerCase();
                        return person.getFullName().toLowerCase().contains(lowerCaseFilter) || 
                               String.valueOf(person.getBirthYear()).contains(lowerCaseFilter);
                    });
                });
                
                personCombo.setItems(filteredPeople);
                
                
                searchContainer.getChildren().addAll(searchField, personCombo);
                grid.add(searchContainer, 1, 4);
                
                Button assignBtn = new Button("Выдать");
                assignBtn.getStyleClass().addAll("button", "book-action-button");
                assignBtn.setOnAction(e -> {
                    PersonDTO selected = personCombo.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        assignBook(selected);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите человека из списка");
                    }
                });
                grid.add(assignBtn, 1, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка загрузки данных");
            errorLabel.getStyleClass().add("error-label");
            grid.add(errorLabel, 1, 4);
        }

        detailStage.setWidth(currentWidth);
    }
    
    private void returnBook() {
        try {
            if (bookService.returnBook(book.getId())) {
                updateAssignmentSection();
                onUpdate.run();
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось вернуть книгу");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при возврате книги");
        }
    }
    
    private void assignBook(PersonDTO person) {
        try {
            if (bookService.assignBook(book.getId(), person.getId())) {
                updateAssignmentSection();
                onUpdate.run();
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось выдать книгу");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при выдаче книги");
        }
    }
    
    private void addControlButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.getStyleClass().add("button-box");
        
        Button editBtn = new Button("Редактировать");
        editBtn.getStyleClass().addAll("button", "book-action-button");
        editBtn.setOnAction(e -> showEditForm());
        
        Button deleteBtn = new Button("Удалить");
        deleteBtn.getStyleClass().addAll("button", "delete-button");
        deleteBtn.setOnAction(e -> handleDelete());

        buttonBox.getChildren().addAll(editBtn, deleteBtn);
        grid.add(buttonBox, 0, 7, 2, 1);
    }
    
    private void showEditForm() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Редактировать книгу");
        stage.setMinWidth(300);

        GridPane editGrid = new GridPane();
        editGrid.getStyleClass().add("admin-form");
        editGrid.setHgap(10);
        editGrid.setVgap(10);
        editGrid.setPadding(new Insets(20));

        TextField nameField = new TextField(book.getName());
        nameField.getStyleClass().add("admin-field");
        TextField authorField = new TextField(book.getAuthor());
        authorField.getStyleClass().add("admin-field");
        TextField yearField = new TextField(String.valueOf(book.getYear()));
        yearField.getStyleClass().add("admin-field");

        editGrid.add(new Label("Название:"), 0, 0);
        editGrid.add(nameField, 1, 0);
        editGrid.add(new Label("Автор:"), 0, 1);
        editGrid.add(authorField, 1, 1);
        editGrid.add(new Label("Год:"), 0, 2);
        editGrid.add(yearField, 1, 2);

        Button saveBtn = new Button("Сохранить");
        saveBtn.getStyleClass().addAll("button", "admin-register-button");
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
        Scene scene = new Scene(editGrid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    
    private void handleDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить эту книгу?");
        confirmation.setContentText(book.getName() + " - " + book.getAuthor());
        confirmation.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (bookService.deleteBook(book.getId())) {
                        onUpdate.run();
                        detailStage.close(); 
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось удалить книгу");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при удалении книги");
                }
            }
        });
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.showAndWait();
    }
}