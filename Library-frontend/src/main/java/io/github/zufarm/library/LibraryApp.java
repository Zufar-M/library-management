package io.github.zufarm.library;

import io.github.zufarm.library.util.SceneManager;
import io.github.zufarm.library.view.auth.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class LibraryApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager.init(primaryStage);
            LoginView loginView = new LoginView();
            SceneManager.switchScene(loginView.getView());
            primaryStage.setTitle("Библиотечная система");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}