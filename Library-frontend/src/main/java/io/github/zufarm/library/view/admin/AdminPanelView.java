package io.github.zufarm.library.view.admin;

import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class AdminPanelView {
    private final UserService userService = new UserService();
    private final TableView<UserDTO> userTable = new TableView<>();
    private final ObservableList<UserDTO> userData = FXCollections.observableArrayList();

    public Parent getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("admin-root");
        
        SplitPane splitPane = new SplitPane(createRegistrationForm(), createUserTable());
        splitPane.getStyleClass().add("admin-split-pane");
        splitPane.setDividerPositions(0.4);
        
        root.setCenter(splitPane);
        return root;
    }

    private GridPane createRegistrationForm() {
        GridPane form = new GridPane();
        form.getStyleClass().addAll("admin-form", "form-grid");
        form.setPadding(new Insets(20));
        form.setHgap(15);
        form.setVgap(10);
        form.setMinWidth(350);

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
        registerButton.setOnAction(e -> registerUser(
            usernameField.getText(),
            passwordField.getText(),
            roleComboBox.getValue()
        ));

        form.add(titleLabel, 0, 0, 2, 1);
        addFormRow(form, "Имя:", usernameField, 1);
        addFormRow(form, "Пароль:", passwordField, 2);
        addFormRow(form, "Роль:", roleComboBox, 4);
        form.add(registerButton, 1, 5, 2, 1);

        return form;
    }

    private void addFormRow(GridPane form, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        label.getStyleClass().add("admin-form-label");
        form.add(label, 0, row);
        form.add(field, 1, row);
    }

    private VBox createUserTable() {
        TableColumn<UserDTO, String> usernameCol = new TableColumn<>("Имя пользователя");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.getStyleClass().add("admin-table-column");
        
        TableColumn<UserDTO, String> roleCol = new TableColumn<>("Роль");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.getStyleClass().add("admin-table-column");
        
        userTable.getColumns().addAll(usernameCol, roleCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setItems(userData);
        userTable.getStyleClass().add("admin-table");

        userTable.setRowFactory(tv -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.getStyleClass().add("admin-table-row");
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    UserDTO selectedUser = row.getItem();
                    showEditDialog(selectedUser);
                }
            });
            return row;
        });

        Button refreshButton = new Button("Обновить список пользователей");
        refreshButton.getStyleClass().addAll("admin-button", "admin-refresh-button");
        refreshButton.setOnAction(e -> loadUsers());
        
        VBox container = new VBox(10, 
            new Label("Список зарегистрированных пользователей"), 
            userTable, 
            refreshButton
        );
        container.getStyleClass().add("admin-table-container");
        container.setPadding(new Insets(20));
        
        loadUsers();
        
        return container;
    }

    private void showEditDialog(UserDTO user) {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.getDialogPane().getStyleClass().addAll("admin-dialog", "dialog-pane");
        dialog.setTitle("Редактирование пользователя");
        dialog.setHeaderText("Измените данные пользователя");

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Удалить", ButtonBar.ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, deleteButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.getStyleClass().addAll("admin-dialog-grid", "dialog-grid");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField(user.getUsername());
        usernameField.getStyleClass().add("admin-dialog-field");
        
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("admin-dialog-field");
        passwordField.setPromptText("Новый пароль (оставьте пустым, чтобы не менять)");
        
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue(user.getRole());
        roleComboBox.getStyleClass().add("admin-dialog-combo");

        grid.add(new Label("Имя пользователя:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Новый пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Роль:"), 0, 2);
        grid.add(roleComboBox, 1, 2);

        // Применяем стили к меткам
        for (int i = 0; i < grid.getChildren().size(); i++) {
            if (grid.getChildren().get(i) instanceof Label) {
                ((Label) grid.getChildren().get(i)).getStyleClass().add("admin-dialog-label");
            }
        }

        dialog.getDialogPane().setContent(grid);

        // Применяем стили к кнопкам
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.getStyleClass().addAll("dialog-button", "button-type-save");
        
        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.getStyleClass().addAll("dialog-button", "button-type-delete");
        
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().addAll("dialog-button", "button-type-cancel");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                user.setUsername(usernameField.getText());
                if (!passwordField.getText().isEmpty()) {
                    user.setPassword(passwordField.getText());
                }
                user.setRole(roleComboBox.getValue());
                return user;
            } else if (dialogButton == deleteButtonType) {
                return new UserDTO(-1, "", "", "");
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result.getId() == -1) {
                deleteUser(user);
            } else {
                try {
                    userService.updateUser(result);
                    loadUsers();
                    showAlert("Успех", "Данные пользователя обновлены", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void deleteUser(UserDTO user) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.getDialogPane().getStyleClass().addAll("admin-dialog", "confirmation-dialog");
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить пользователя?");
        confirmation.setContentText("Пользователь: " + user.getUsername());

        // Применяем стили к кнопкам
        ButtonType okButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;
        
        confirmation.getButtonTypes().setAll(okButton, cancelButton);
        
        Button okBtn = (Button) confirmation.getDialogPane().lookupButton(okButton);
        okBtn.getStyleClass().addAll("dialog-button", "button-type-delete");
        
        Button cancelBtn = (Button) confirmation.getDialogPane().lookupButton(cancelButton);
        cancelBtn.getStyleClass().addAll("dialog-button", "button-type-cancel");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.deleteUser(user.getId());
                    loadUsers();
                    showAlert("Успех", "Пользователь успешно удален", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void registerUser(String username, String password, String role) {
        try {
            UserDTO newUser = new UserDTO(0, username, role, password);
            userService.registerUser(newUser);
            loadUsers();
            showAlert("Успех", "Пользователь успешно зарегистрирован", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadUsers() {
        userData.clear();
        userData.addAll(userService.getAllUsers());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.getDialogPane().getStyleClass().addAll("admin-dialog", "dialog-pane");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Стилизация кнопки OK в алерте
        ButtonType okButton = ButtonType.OK;
        Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
        okBtn.getStyleClass().addAll("dialog-button", "button-type-save");
        
        alert.showAndWait();
    }
}