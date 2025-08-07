package io.github.zufarm.library.view.admin;
import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.services.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminAddView {
    private final UserService userService = new UserService();
    
    public void showForm(Runnable onSuccess) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Регистрация нового пользователя");
        stage.setMinWidth(350);

        GridPane grid = new GridPane();
        grid.getStyleClass().addAll("admin-form", "form-grid");
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(10);

        Label titleLabel = new Label("Регистрация нового пользователя");
        titleLabel.getStyleClass().add("admin-form-title");
        
        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("admin-field");
        
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("admin-field");
        
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue("Сотрудник");
        roleComboBox.getStyleClass().add("admin-combo-box");
        
        Button registerButton = new Button("Зарегистрировать");
        registerButton.getStyleClass().addAll("admin-button", "admin-register-button");
        registerButton.setOnAction(e -> {
            try {
                UserDTO newUser = new UserDTO(0, usernameField.getText(), 
                    roleComboBox.getValue(), passwordField.getText());
                userService.registerUser(newUser);
                onSuccess.run();
                showAlert("Успех", "Пользователь успешно зарегистрирован", Alert.AlertType.INFORMATION);
                stage.close();
            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        grid.add(titleLabel, 0, 0, 2, 1);
        addFormRow(grid, "Имя:", usernameField, 1);
        addFormRow(grid, "Пароль:", passwordField, 2);
        addFormRow(grid, "Роль:", roleComboBox, 4);
        grid.add(registerButton, 1, 5, 2, 1);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void addFormRow(GridPane form, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        label.getStyleClass().add("admin-form-label");
        form.add(label, 0, row);
        form.add(field, 1, row);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.getDialogPane().getStyleClass().addAll("admin-dialog", "dialog-pane");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ButtonType okButton = ButtonType.OK;
        Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
        okBtn.getStyleClass().addAll("dialog-button", "button-type-save");
        
        alert.showAndWait();
    }
}