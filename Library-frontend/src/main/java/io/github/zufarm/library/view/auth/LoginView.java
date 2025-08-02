package io.github.zufarm.library.view.auth;

import io.github.zufarm.library.controllers.AuthController;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

public class LoginView {
    private Parent view;
    
    public LoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        
        // Получаем URL ресурса (важно: путь должен быть правильным)
        URL fxmlUrl = getClass().getResource("/fxml/auth/login.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Cannot find FXML file: /fxml/auth/login.fxml");
        }
        
        loader.setLocation(fxmlUrl);
        loader.setController(new AuthController());
        view = loader.load();
    }
    
    public Parent getView() {
        return view;
    }
}