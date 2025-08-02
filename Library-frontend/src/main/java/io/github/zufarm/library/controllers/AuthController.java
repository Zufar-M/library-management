package io.github.zufarm.library.controllers;

import io.github.zufarm.library.dto.JwtResponse;
import io.github.zufarm.library.dto.LoginRequest;
import io.github.zufarm.library.services.AuthService;
import io.github.zufarm.library.util.SceneManager;
import io.github.zufarm.library.view.MainView;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AuthController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    private final AuthService authService = new AuthService();
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Логин и пароль обязательны");
            return;
        }
        
        try {
            LoginRequest request = new LoginRequest(username, password);
            JwtResponse response = authService.login(request);
            if (response != null && response.getToken() != null) {
                // Успешная авторизация
            	
                SceneManager.switchScene(new MainView().getView());
            } else {
                errorLabel.setText("Неверный логин или пароль");
            }
        } catch (Exception e) {
            errorLabel.setText("Ошибка соединения с сервером");
            e.printStackTrace();
        }
    }
}