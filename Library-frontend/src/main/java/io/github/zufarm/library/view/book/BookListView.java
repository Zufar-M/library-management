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
        titleCol.getStyleClass().add("table-column");
        
        TableColumn<BookDTO, String> authorCol = new TableColumn<>("Автор");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.getStyleClass().add("table-column");
        
        TableColumn<BookDTO, Integer> yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.getStyleClass().add("table-column");
        
        table.getColumns().addAll(titleCol, authorCol, yearCol);
        table.setItems(filteredBooks);
        table.getStyleClass().add("book-table");
        
        table.setRowFactory(tv -> {
            TableRow<BookDTO> row = new TableRow<>();
            row.getStyleClass().add("table-row");
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
        layout.getStyleClass().add("book-list-container");
        
        searchField = new TextField();
        searchField.setPromptText("Поиск по названию...");
        searchField.getStyleClass().add("search-field");
        
        Button addBookBtn = new Button("Добавить книгу");
        addBookBtn.getStyleClass().addAll("button", "add-button");
        addBookBtn.setOnAction(e -> {
            new BookAddView().showForm(this::loadBooks);
        });
        
        HBox buttonPanel = new HBox(10);
        buttonPanel.getStyleClass().add("button-panel");
        buttonPanel.getChildren().addAll(addBookBtn);
        
        layout.getChildren().addAll(searchField, table, buttonPanel);
        return layout;
    }
}