package io.github.zufarm.library.view.person;

import io.github.zufarm.library.services.PersonService;
import io.github.zufarm.library.dto.PersonDTO;
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

public class PersonListView {
    private final PersonService personService = new PersonService();
    private final TableView<PersonDTO> table = new TableView<>();
    private final ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    private final FilteredList<PersonDTO> filteredPeople = new FilteredList<>(people);
    private TextField searchField;
    
    public PersonListView() {
        initializeTable();
        loadPeople();
    }
    
    private void initializeTable() {
        table.getStyleClass().add("person-table");
        
        TableColumn<PersonDTO, String> fullNameCol = new TableColumn<>("Имя");
        fullNameCol.getStyleClass().add("table-column");
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<PersonDTO, Integer> birthYearCol = new TableColumn<>("Год рождения");
        birthYearCol.getStyleClass().add("table-column");
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        
        table.getColumns().addAll(fullNameCol, birthYearCol);
        table.setItems(filteredPeople);
        table.setRowFactory(tv -> {
            TableRow<PersonDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    PersonDTO rowData = row.getItem();
                    new PersonDetailView().showDetail(rowData, this::loadPeople);
                }
            });
            return row;
        });
    }
    
    private void loadPeople() {
        try {
            people.setAll(personService.getAllPeople());
            if (searchField != null) {
                searchField.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            people.clear();
        }
    }
    
    public Parent getView() {
        VBox layout = new VBox(10);
        layout.getStyleClass().add("person-list-container");
        
        searchField = new TextField();
        searchField.getStyleClass().add("search-field");
        searchField.setPromptText("Поиск по имени...");
        
        Button refreshBtn = new Button("Обновить");
        refreshBtn.getStyleClass().add("refresh-button");
        refreshBtn.setOnAction(e -> loadPeople());
        
        Button addPersonBtn = new Button("Добавить нового читателя");
        addPersonBtn.getStyleClass().add("add-button");
        addPersonBtn.setOnAction(e -> {
            new PersonAddView().showForm(this::loadPeople);
        });
        
        HBox buttonPanel = new HBox(10);
        buttonPanel.getStyleClass().add("button-panel");
        buttonPanel.getChildren().addAll(refreshBtn, addPersonBtn);
        
        layout.getChildren().addAll(searchField, table, buttonPanel);
        return layout;
    }
}