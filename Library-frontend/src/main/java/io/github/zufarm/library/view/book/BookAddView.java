package io.github.zufarm.library.view.book;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.services.BookService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookAddView {
    private final BookService bookService = new BookService();

    public void showForm(Runnable onSuccess) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Добавить книгу");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField authorField = new TextField();
        TextField yearField = new TextField();

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Год:"), 0, 2);
        grid.add(yearField, 1, 2);

        Button submitBtn = new Button("Сохранить");
        submitBtn.setOnAction(e -> {
            try {
                BookDTO book = new BookDTO(nameField.getText(), authorField.getText(), Integer.parseInt(yearField.getText()) );

                bookService.addBook(book);
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