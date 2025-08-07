package io.github.zufarm.library.controllers;

import io.github.zufarm.library.dto.LoginRequest;
import io.github.zufarm.library.dto.LoginResponse;
import io.github.zufarm.library.services.AuthService;
import io.github.zufarm.library.util.JwtTokenUtil;
import io.github.zufarm.library.util.SceneManager;
import io.github.zufarm.library.view.MainView;
import javafx.scene.control.*;

public class AuthController {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public void setup(TextField usernameField, 
                    PasswordField passwordField, 
                    Label errorLabel,
                    Button loginButton) {
        
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.errorLabel = errorLabel;
        
        loginButton.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Логин и пароль обязательны");
            return;
        }
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = authService.authenticate(request);
            if (response.getError() != null) {
            	showError(response.getError());
            }
            else {
            JwtTokenUtil.initValues(response);
            SceneManager.switchScene(new MainView().getView());
            }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}