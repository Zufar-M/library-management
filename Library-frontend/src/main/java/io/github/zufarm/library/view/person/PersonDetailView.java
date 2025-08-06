package io.github.zufarm.library.view.person;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.BookService;
import io.github.zufarm.library.services.PersonService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PersonDetailView {
    private final PersonService personService = new PersonService();
    private final BookService bookService = new BookService();
    private PersonDTO person;
    private Stage detailStage;
    private Runnable onUpdate;
    private TableView<BookDTO> booksTable;
    
    public void showDetail(PersonDTO person, Runnable onUpdate) {
        this.person = person;
        this.onUpdate = onUpdate;
        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Детали читателя");
        detailStage.setMinWidth(600);
        detailStage.setMinHeight(400);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("detail-grid");
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        col1.setPrefWidth(120);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setFillWidth(true);
        
        grid.getColumnConstraints().addAll(col1, col2);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = person.getBirthDate().format(dateFormatter);

        Label nameLabel = new Label(person.getFullName());
        nameLabel.getStyleClass().add("detail-value");
        Label birthDateLabel = new Label(formattedDate);
        birthDateLabel.getStyleClass().add("detail-value");
        Label phoneLabel = new Label(person.getPhoneNumber() != null ? person.getPhoneNumber() : "Не указан");
        phoneLabel.getStyleClass().add("detail-value");

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new Label("Дата рождения:"), 0, 1);
        grid.add(birthDateLabel, 1, 1);
        grid.add(new Label("Телефон:"), 0, 2);
        grid.add(phoneLabel, 1, 2);

        Label booksLabel = new Label("Книги на руках:");
        booksLabel.getStyleClass().add("section-title");
        grid.add(booksLabel, 0, 3, 2, 1);

        booksTable = new TableView<>();
        booksTable.getStyleClass().add("detail-table");
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        booksTable.setPlaceholder(new Label("Нет книг на руках"));
        
        TableColumn<BookDTO, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        
        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        
        TableColumn<BookDTO, Number> yearColumn = new TableColumn<>("Год");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
        
        booksTable.getColumns().addAll(nameColumn, authorColumn, yearColumn);
        
        booksTable.setRowFactory(tv -> {
            TableRow<BookDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    BookDTO selectedBook = row.getItem();
                    showBookDetails(selectedBook);
                }
            });
            return row;
        });

        refreshBooksTable();

        ScrollPane scrollPane = new ScrollPane(booksTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("detail-scroll");
        
        grid.add(scrollPane, 0, 4, 2, 1);
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        GridPane.setVgrow(scrollPane, Priority.ALWAYS);

        HBox buttonBox = new HBox(15);
        buttonBox.getStyleClass().add("button-box");
        
        Button editBtn = new Button("Редактировать");
        editBtn.getStyleClass().add("edit-button");
        editBtn.setOnAction(e -> showEditForm(detailStage, onUpdate));
        
        Button deleteBtn = new Button("Удалить");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> handleDelete(onUpdate));

        buttonBox.getChildren().addAll(editBtn, deleteBtn);
        grid.add(buttonBox, 1, 5);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        detailStage.setScene(scene);
        detailStage.show();
    }

    private void showBookDetails(BookDTO book) {
        Stage bookStage = new Stage();
        bookStage.initModality(Modality.APPLICATION_MODAL);
        bookStage.setTitle("Детали книги");
        
        GridPane grid = new GridPane();
        grid.getStyleClass().add("book-detail-grid");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Название:"), 0, 0);
        Label nameLabel = new Label(book.getName());
        nameLabel.getStyleClass().add("book-detail-value");
        grid.add(nameLabel, 1, 0);
        
        grid.add(new Label("Автор:"), 0, 1);
        Label authorLabel = new Label(book.getAuthor());
        authorLabel.getStyleClass().add("book-detail-value");
        grid.add(authorLabel, 1, 1);
        
        grid.add(new Label("Год:"), 0, 2);
        Label yearLabel = new Label(String.valueOf(book.getYear()));
        yearLabel.getStyleClass().add("book-detail-value");
        grid.add(yearLabel, 1, 2);

        Button returnBtn = new Button("Вернуть книгу");
        returnBtn.getStyleClass().add("return-button");
        returnBtn.setOnAction(e -> {
            if (bookService.returnBook(book.getId())) {
                refreshBooks();
                bookStage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Не удалось вернуть книгу").show();
            }
        });

        grid.add(returnBtn, 1, 3);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        bookStage.setScene(scene);
        bookStage.show();
    }

    private void refreshBooks() {
        refreshBooksTable();
        if (onUpdate != null) {
            onUpdate.run();
        }
    }

    private void refreshBooksTable() {
        List<BookDTO> heldBooks = bookService.getBooksByHolder(person.getId());
        booksTable.getItems().setAll(heldBooks);
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
        stage.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("edit-form-grid");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(person.getFullName());
        nameField.getStyleClass().add("edit-field");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        TextField birthDateField = new TextField(person.getBirthDate().format(dateFormatter));
        birthDateField.getStyleClass().add("edit-field");
        birthDateField.setPromptText("дд.мм.гггг");

        TextField phoneField = new TextField(person.getPhoneNumber());
        phoneField.getStyleClass().add("edit-field");
        phoneField.setPromptText("+7 XXX XXX-XX-XX");

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Дата рождения:"), 0, 1);
        grid.add(birthDateField, 1, 1);
        grid.add(new Label("Телефон:"), 0, 2);
        grid.add(phoneField, 1, 2);

        Button saveBtn = new Button("Сохранить");
        saveBtn.getStyleClass().add("save-button");
        saveBtn.setOnAction(e -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate birthDate = LocalDate.parse(birthDateField.getText(), formatter);
                
                person.setFullName(nameField.getText());
                person.setBirthDate(birthDate);
                person.setPhoneNumber(phoneField.getText());
                
                if (personService.updatePerson(person)) {
                    onUpdate.run();
                    parentStage.close();
                    stage.close();
                    showDetail(person, onUpdate);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при обновлении читателя").show();
                }
            } catch (DateTimeParseException ex) {
                new Alert(Alert.AlertType.ERROR, "Ошибка: Некорректный формат даты. Используйте дд.мм.гггг").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Ошибка: " + ex.getMessage()).show();
            }
        });

        grid.add(saveBtn, 1, 3);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}