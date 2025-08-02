package io.github.zufarm.library.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static String currentToken;
    
    public static void init(Stage stage) {
        primaryStage = stage;
    }
    
    public static void switchScene(Parent root) {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
    
    public static void setCurrentToken(String token) {
        currentToken = token;
    }
    
    public static String getCurrentToken() {
        return currentToken;
    }
    public static void navigateTo(String viewName) {
        switch (viewName) {
            case "books":
                break;
            case "addBook":
                break;
            default:
                break;
        }
    }
    
}