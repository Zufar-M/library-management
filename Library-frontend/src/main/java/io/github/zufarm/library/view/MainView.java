package io.github.zufarm.library.view;

import io.github.zufarm.library.util.SceneManager;
import io.github.zufarm.library.view.book.BookListView;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class MainView {
    private Parent view;
    
    public MainView() {
        createView();
    }
    
    private void createView() {
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        
        // Вкладка "Книги"
        Tab booksTab = new Tab("Книги");
        booksTab.setClosable(false);
        booksTab.setContent(new BookListView().getView()); // Загружаем список книг сразу
        
        // Вкладка "Читатели" (заглушка)
        Tab readersTab = new Tab("Читатели");
        readersTab.setClosable(false);
        readersTab.setContent(new Label("Раздел читателей в разработке"));
        
        tabPane.getTabs().addAll(booksTab, readersTab);
        root.setCenter(tabPane);
        
        view = root;
    }
    
    public Parent getView() {
        return view;
    }
}