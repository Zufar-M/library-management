package io.github.zufarm.library.view.book;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.services.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BookListView {
    private final BookService bookService = new BookService();
    private final TableView<BookDTO> table = new TableView<>();
    private final ObservableList<BookDTO> books = FXCollections.observableArrayList();
    private final FilteredList<BookDTO> filteredBooks = new FilteredList<>(books);
    private TextField searchField;
    
    public BookListView() {
        initializeTable();
        loadBooks();
    }
    
    private void initializeTable() {
        TableColumn<BookDTO, String> titleCol = new TableColumn<>("Название");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<BookDTO, String> authorCol = new TableColumn<>("Автор");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        TableColumn<BookDTO, Integer> yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        table.getColumns().addAll(titleCol, authorCol, yearCol);
        table.setItems(filteredBooks);
        table.setRowFactory(tv -> {
            TableRow<BookDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    BookDTO rowData = row.getItem();
                    new BookDetailView().showDetail(rowData, this::loadBooks);
                }
            });
            return row;
        });
    }
    
    private void loadBooks() {
        try {
            books.setAll(bookService.getAllBooks());
            if (searchField != null) {
                searchField.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            books.clear();
        }
    }
    
    public Parent getView() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        
        searchField = new TextField();
        searchField.setPromptText("Поиск по названию...");
        searchField.setPrefWidth(200);
        searchField.setMaxWidth(200);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBooks.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return book.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        
        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> loadBooks());
        
        Button addBookBtn = new Button("Добавить книгу");
        addBookBtn.setOnAction(e -> {
            new BookAddView().showForm(this::loadBooks);
        });
        
        HBox buttonPanel = new HBox(10);
        buttonPanel.getChildren().addAll(refreshBtn, addBookBtn);
        
        layout.getChildren().addAll(searchField, table, buttonPanel);
        return layout;
    }
}