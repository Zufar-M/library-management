package io.github.zufarm.library.view.admin;
import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.services.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminDetailView {
    private final UserService userService = new UserService();
    private UserDTO user;
    private Stage detailStage;
    private Runnable onUpdate;
    private GridPane grid;

    public void showDetail(UserDTO user, Runnable onUpdate) {
        this.user = user;
        this.onUpdate = onUpdate;
        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Редактирование пользователя");
        detailStage.setMinWidth(400);

        grid = new GridPane();
        grid.getStyleClass().add("admin-form");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(150);
        col1.setPrefWidth(150);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(200);
        col2.setPrefWidth(200);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        addUserInfo();
        addControlButtons();

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        detailStage.setScene(scene);
        detailStage.show();
    }

    private void addUserInfo() {
        Label titleLabel = new Label("Редактирование пользователя");
        titleLabel.getStyleClass().add("admin-form-title");
        grid.add(titleLabel, 0, 0, 2, 1);

        TextField usernameField = new TextField(user.getUsername());
        usernameField.getStyleClass().add("admin-field");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("admin-field");
        passwordField.setPromptText("Новый пароль");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue(user.getRole());
        roleComboBox.getStyleClass().add("admin-field");

        grid.add(new Label("Имя пользователя:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Новый пароль:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Роль:"), 0, 3);
        grid.add(roleComboBox, 1, 3);
    }

    private void addControlButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.getStyleClass().add("button-box");

        Button saveBtn = new Button("Сохранить");
        saveBtn.getStyleClass().addAll("button", "admin-register-button");
        saveBtn.setOnAction(e -> {
            try {
                user.setUsername(((TextField) grid.getChildren().get(2)).getText());
                String newPassword = ((PasswordField) grid.getChildren().get(4)).getText();
                if (!newPassword.isEmpty()) {
                    user.setPassword(newPassword);
                }
                user.setRole(((ComboBox<String>) grid.getChildren().get(6)).getValue());

                userService.updateUser(user);
                onUpdate.run();
                showAlert("Успех", "Данные пользователя обновлены", Alert.AlertType.INFORMATION);
                detailStage.close();
            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button deleteBtn = new Button("Удалить");
        deleteBtn.getStyleClass().addAll("button", "delete-button");
        deleteBtn.setOnAction(e -> confirmAndDelete());

        buttonBox.getChildren().addAll(saveBtn, deleteBtn);
        grid.add(buttonBox, 0, 4, 2, 1);
    }

    private void confirmAndDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить пользователя?");
        confirmation.setContentText("Пользователь: " + user.getUsername());
        confirmation.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.deleteUser(user.getId());
                    onUpdate.run();
                    showAlert("Успех", "Пользователь успешно удален", Alert.AlertType.INFORMATION);
                    detailStage.close();
                } catch (Exception e) {
                    showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.showAndWait();
    }
}