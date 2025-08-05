package io.github.zufarm.library.view.admin;
import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminListView {
    private final UserService userService = new UserService();
    private final TableView<UserDTO> table = new TableView<>();
    private final ObservableList<UserDTO> users = FXCollections.observableArrayList();
    
    public AdminListView() {
        initializeTable();
        loadUsers();
    }
    
    private void initializeTable() {
        table.getStyleClass().add("admin-table");
        
        TableColumn<UserDTO, String> usernameCol = new TableColumn<>("Имя пользователя");
        usernameCol.getStyleClass().add("table-column");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<UserDTO, String> roleCol = new TableColumn<>("Роль");
        roleCol.getStyleClass().add("table-column"); 
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        table.getColumns().addAll(usernameCol, roleCol);
        table.setItems(users);
        table.setRowFactory(tv -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.getStyleClass().add("admin-table-row");
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    UserDTO selectedUser = row.getItem();
                    new AdminDetailView().showDetail(selectedUser, this::loadUsers);
                }
            });
            return row;
        });
    }
    
    private void loadUsers() {
        try {
            users.setAll(userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            users.clear();
        }
    }
    
    public Parent getView() {
        VBox layout = new VBox(10);
        layout.getStyleClass().add("admin-list-container");
        layout.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Список зарегистрированных пользователей");
        titleLabel.getStyleClass().add("admin-title");
        
        Button addUserButton = new Button("Добавить нового пользователя");
        addUserButton.getStyleClass().addAll("button", "add-button"); 
        addUserButton.setOnAction(e -> new AdminAddView().showForm(this::loadUsers));
        
        HBox buttonPanel = new HBox(10);
        buttonPanel.getStyleClass().add("button-panel"); 
        buttonPanel.getChildren().addAll(addUserButton);
        
        layout.getChildren().addAll(titleLabel, table, buttonPanel);
        return layout;
    }
}