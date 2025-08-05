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
        grid.getStyleClass().add("admin-form");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label titleLabel = new Label("Добавить новую книгу");
        titleLabel.getStyleClass().add("admin-form-title");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label nameLabel = new Label("Название:");
        nameLabel.getStyleClass().add("admin-form-label");
        TextField nameField = new TextField();
        nameField.getStyleClass().add("admin-field");
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);

        Label authorLabel = new Label("Автор:");
        authorLabel.getStyleClass().add("admin-form-label");
        TextField authorField = new TextField();
        authorField.getStyleClass().add("admin-field");
        grid.add(authorLabel, 0, 2);
        grid.add(authorField, 1, 2);

        Label yearLabel = new Label("Год:");
        yearLabel.getStyleClass().add("admin-form-label");
        TextField yearField = new TextField();
        yearField.getStyleClass().add("admin-field");
        grid.add(yearLabel, 0, 3);
        grid.add(yearField, 1, 3);

        Button submitBtn = new Button("Сохранить");
        submitBtn.getStyleClass().addAll("admin-button", "admin-register-button");
        submitBtn.setOnAction(e -> {
            try {
                BookDTO book = new BookDTO(0, nameField.getText(), authorField.getText(), Integer.parseInt(yearField.getText()));
                bookService.addBook(book);
                onSuccess.run();
                stage.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: " + ex.getMessage());
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                alert.show();
            }
        });

        grid.add(submitBtn, 1, 4);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}