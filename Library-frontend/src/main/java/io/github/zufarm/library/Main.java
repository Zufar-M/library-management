package io.github.zufarm.library;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    private TextArea responseArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Auth Client");

        // Создаем элементы интерфейса
        Button sendButton = new Button("Send Login Request");
        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setWrapText(true);

        // Обработчик нажатия кнопки
        sendButton.setOnAction(e -> sendLoginRequest());

        // Настраиваем layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(sendButton, responseArea);

        // Создаем сцену и показываем окно
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendLoginRequest() {
        try {
            // URL для запроса
            URL url = new URL("http://localhost:8080/library/auth/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Настраиваем соединение
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Тело запроса
            String requestBody = "{\"username\": \"AdminUser\", \"password\": \"12345\"}";

            // Отправляем запрос
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Получаем ответ
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) { // Успешный ответ
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
            } else { // Ошибка
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
            }

            // Выводим результат в текстовое поле
            responseArea.setText("Response Code: " + responseCode + "\n\nResponse Body:\n" + response.toString());

        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
