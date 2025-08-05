package io.github.zufarm.library.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    
    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
    }
    
    public static void switchScene(Parent root) {
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(SceneManager.class.getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}