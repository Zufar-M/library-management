package io.github.zufarm.library.view.book;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class BookAddView {
    private Parent view;
    
    public BookAddView() {
        GridPane form = new GridPane();
        // Поля формы для добавления книги
        view = form;
    }
    
    public Parent getView() {
        return view;
    }
}