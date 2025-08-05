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
        root.setMinWidth(800);
        
        SplitPane splitPane = new SplitPane(createRegistrationForm(), createUserTable());
        splitPane.setDividerPositions(0.4);
        splitPane.setPrefWidth(800);
        
        root.setCenter(splitPane);
        return root;
    }

    private GridPane createRegistrationForm() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setHgap(15);
        form.setVgap(10);
        form.setStyle("-fx-background-color: #f5f5f5;");
        form.setMinWidth(350);
        form.setPrefWidth(350);

        Label titleLabel = new Label("Регистрация нового пользователя");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        TextField usernameField = new TextField();
        usernameField.setMinWidth(200);
        usernameField.setPrefWidth(200);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setMinWidth(200);
        passwordField.setPrefWidth(200);
        
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue("Сотрудник");
        roleComboBox.setMinWidth(200);
        roleComboBox.setPrefWidth(200);
        
        Button registerButton = new Button("Зарегистрировать");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        registerButton.setMinWidth(200);
        registerButton.setPrefWidth(200);
        registerButton.setMinHeight(30);
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
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setPrefWidth(120);
        label.setStyle("-fx-font-size: 14;");
        form.add(label, 0, row);
        form.add(field, 1, row);
    }

    private VBox createUserTable() {
        TableColumn<UserDTO, String> usernameCol = new TableColumn<>("Имя пользователя");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(200);
        usernameCol.setStyle("-fx-font-size: 14;");
        
        TableColumn<UserDTO, String> roleCol = new TableColumn<>("Роль");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(150);
        roleCol.setStyle("-fx-font-size: 14;");
        
        userTable.getColumns().addAll(usernameCol, roleCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setItems(userData);
        userTable.setPrefWidth(450);
        userTable.setStyle("-fx-font-size: 14;");

        userTable.setRowFactory(tv -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    UserDTO selectedUser = row.getItem();
                    showEditDialog(selectedUser);
                }
            });
            return row;
        });

        Button refreshButton = new Button("Обновить список пользователей");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        refreshButton.setMinWidth(180);
        refreshButton.setMinHeight(30);
        refreshButton.setOnAction(e -> loadUsers());
        
        VBox container = new VBox(10, new Label("Список зарегистрированных пользователей"), userTable, refreshButton);
        container.setPadding(new Insets(20));
        container.setPrefWidth(450);
        
        loadUsers();
        
        return container;
    }

    private void showEditDialog(UserDTO user) {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Редактирование пользователя");
        dialog.setHeaderText("Измените данные пользователя");

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Удалить", ButtonBar.ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, deleteButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField(user.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Новый пароль (оставьте пустым, чтобы не менять)");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue(user.getRole());

        grid.add(new Label("Имя пользователя:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Новый пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Роль:"), 0, 2);
        grid.add(roleComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

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
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Вы уверены, что хотите удалить пользователя?");
        confirmation.setContentText("Пользователь: " + user.getUsername());

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
        }  catch (Exception e) {
            showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadUsers() {
        userData.clear();
        userData.addAll(userService.getAllUsers());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(350);
        dialogPane.setMinHeight(150);
        dialogPane.setStyle("-fx-font-size: 14;");
        
        alert.showAndWait();
    }
}