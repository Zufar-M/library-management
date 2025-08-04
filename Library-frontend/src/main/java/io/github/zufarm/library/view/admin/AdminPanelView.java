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
        
        
        root.setMinWidth(900);
        
        SplitPane splitPane = new SplitPane(createRegistrationForm(), createUserTable());
        splitPane.setDividerPositions(0.35);
        splitPane.setPrefWidth(900);
        
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
        usernameField.setMinWidth(250);
        usernameField.setPrefWidth(250);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setMinWidth(250);
        passwordField.setPrefWidth(250);
        
        TextField birthYearField = new TextField();
        birthYearField.setMinWidth(250);
        birthYearField.setPrefWidth(250);
        
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Сотрудник", "Администратор");
        roleComboBox.setValue("Сотрудник");
        roleComboBox.setMinWidth(250);
        roleComboBox.setPrefWidth(250);
        
        Button registerButton = new Button("Зарегистрировать");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        registerButton.setMinWidth(250);
        registerButton.setPrefWidth(250);
        registerButton.setOnAction(e -> registerUser(
            usernameField.getText(),
            passwordField.getText(),
            birthYearField.getText(),
            roleComboBox.getValue()
        ));

    
        form.add(titleLabel, 0, 0, 2, 1);
        
        addFormRow(form, "Имя пользователя:", usernameField, 1);
        addFormRow(form, "Пароль:", passwordField, 2);
        addFormRow(form, "Год рождения:", birthYearField, 3);
        addFormRow(form, "Роль:", roleComboBox, 4);
        
        form.add(registerButton, 0, 5, 2, 1);

        return form;
    }

    private void addFormRow(GridPane form, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setPrefWidth(100);
        form.add(label, 0, row);
        form.add(field, 1, row);
    }

    private VBox createUserTable() {
   
        TableColumn<UserDTO, String> usernameCol = new TableColumn<>("Имя пользователя");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(200);
        
        TableColumn<UserDTO, Integer> birthYearCol = new TableColumn<>("Год рождения");
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfBirth"));
        birthYearCol.setPrefWidth(100);
        
        TableColumn<UserDTO, String> roleCol = new TableColumn<>("Роль");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(150);
        
        userTable.getColumns().addAll(usernameCol, birthYearCol, roleCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setItems(userData);
        userTable.setPrefWidth(550);
        
   
        Button refreshButton = new Button("Обновить список");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        refreshButton.setOnAction(e -> loadUsers());
        
        VBox container = new VBox(10, new Label("Зарегистрированные пользователи"), userTable, refreshButton);
        container.setPadding(new Insets(20));
        container.setPrefWidth(550);
        
        loadUsers();
        
        return container;
    }

    private void registerUser(String username, String password, String birthYear, String role) {
        try {
            int year = Integer.parseInt(birthYear);
            UserDTO newUser = new UserDTO(0, username, year, role, password);
            userService.registerUser(newUser);
            loadUsers();
            showAlert("Успех", "Пользователь успешно зарегистрирован", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Год рождения должен быть числом", Alert.AlertType.ERROR);
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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(400);
        dialogPane.setMinHeight(150);
        
        alert.showAndWait();
    }
}