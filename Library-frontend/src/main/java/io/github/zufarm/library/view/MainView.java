package io.github.zufarm.library.view;
import io.github.zufarm.library.util.JwtTokenUtil;
import io.github.zufarm.library.util.SceneManager;
import io.github.zufarm.library.view.admin.AdminListView;
import io.github.zufarm.library.view.auth.LoginView;
import io.github.zufarm.library.view.book.BookListView;
import io.github.zufarm.library.view.person.PersonListView;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;


public class MainView {
    private Parent view;
    
    public MainView() {
        createView();
    }
    
    private void createView() {
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        
        Button logoutButton = new Button("Выход");
        logoutButton.setId("logoutButton"); 
        logoutButton.setOnAction(event -> {
            JwtTokenUtil.clearToken();
            SceneManager.switchScene(new LoginView().getView());
        });
        
        HBox topBar = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, logoutButton);
        
        Tab booksTab = new Tab("Книги");
        booksTab.setClosable(false);
        booksTab.setContent(new BookListView().getView()); 
        
        Tab readersTab = new Tab("Читатели");
        readersTab.setClosable(false);
        readersTab.setContent(new PersonListView().getView());
        
        tabPane.getTabs().addAll(booksTab, readersTab);
        
        if (JwtTokenUtil.getRole().equals("ROLE_ADMIN")) {
            Tab adminTab = new Tab("Администрирование");
            adminTab.setClosable(false);
            adminTab.setContent(new AdminListView().getView());
            tabPane.getTabs().add(adminTab);
        }
        
        root.setTop(topBar);
        root.setCenter(tabPane);
        root.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        view = root;
    }
    
    public Parent getView() {
        return view;
    }
}