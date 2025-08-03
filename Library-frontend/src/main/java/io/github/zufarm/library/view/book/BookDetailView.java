package io.github.zufarm.library.view.book;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.services.BookService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookDetailView {
    private final BookService bookService = new BookService();
    private BookDTO book;
    private Stage detailStage;
    
    public void showDetail(BookDTO book, Runnable onUpdate) {
        this.book = book;
        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Детали книги");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label nameLabel = new Label(book.getName());
        Label authorLabel = new Label(book.getAuthor());
        Label yearLabel = new Label(String.valueOf(book.getYear()));

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorLabel, 1, 1);
        grid.add(new Label("Год:"), 0, 2);
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
        confirmation.setHeaderText("Вы уверены, что хотите удалить эту книгу?");
        confirmation.setContentText(book.getName() + " - " + book.getAuthor());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bookService.deleteBook(book.getId())) {
                    onUpdate.run();
                    detailStage.close(); 
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при удалении книги").show();
                }
            }
        });
    }

    private void showEditForm(Stage parentStage, Runnable onUpdate) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Редактировать книгу");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(book.getName());
        TextField authorField = new TextField(book.getAuthor());
        TextField yearField = new TextField(String.valueOf(book.getYear()));

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Год:"), 0, 2);
        grid.add(yearField, 1, 2);

        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> {
            try {
         
                book.setName(nameField.getText());
                book.setAuthor(authorField.getText());
                book.setYear(Integer.parseInt(yearField.getText()));     
                
                if (bookService.updateBook(book)) {
                    onUpdate.run();
                    parentStage.close();
                    stage.close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Ошибка при обновлении книги").show();
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