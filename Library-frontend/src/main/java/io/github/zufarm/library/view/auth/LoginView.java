package io.github.zufarm.library.view.auth;
import io.github.zufarm.library.controllers.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginView {
    private final VBox view;
    private final AuthController controller;

    public LoginView() {
        this.controller = new AuthController();
        this.view = createView();
    }

    private VBox createView() {
        Text title = new Text("Вход в систему");
        title.getStyleClass().add("login-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Введите логин");
        usernameField.getStyleClass().add("login-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Введите пароль");
        passwordField.getStyleClass().add("login-field");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("login-error");

        Button loginButton = new Button("Войти");
        loginButton.getStyleClass().add("login-button");
        loginButton.setDefaultButton(true);

        
        controller.setup(usernameField, passwordField, errorLabel, loginButton);

        VBox layout = new VBox(10, 
            title,
            new Label("Логин:"),
            usernameField,
            new Label("Пароль:"),
            passwordField,
            errorLabel,
            loginButton
        );
        
        layout.getStyleClass().add("login-container");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return layout;
    }

    public Parent getView() {
        return view;
    }
}