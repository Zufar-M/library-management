package io.github.zufarm.library.view.person;

import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.services.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;

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
        
        TableColumn<PersonDTO, String> fullNameCol = new TableColumn<>("ФИО");
        fullNameCol.getStyleClass().add("table-column");
        fullNameCol.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        
        TableColumn<PersonDTO, String> birthDateCol = new TableColumn<>("Дата рождения");
        birthDateCol.getStyleClass().add("table-column");
        birthDateCol.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getBirthDate().format(formatter)
            );
        });
        
        TableColumn<PersonDTO, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.getStyleClass().add("table-column");
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
             
        table.getColumns().addAll(fullNameCol, birthDateCol, phoneCol);
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
        searchField.setPromptText("Поиск по ФИО или телефону...");
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredPeople.setPredicate(person -> {
                if (newVal == null || newVal.isEmpty()) return true;
                
                String lowerCaseFilter = newVal.toLowerCase();
                return person.getFullName().toLowerCase().contains(lowerCaseFilter) ||
                      (person.getPhoneNumber() != null && 
                       person.getPhoneNumber().toLowerCase().contains(lowerCaseFilter));
            });
        });
        
        Button addPersonBtn = new Button("Добавить читателя");
        addPersonBtn.getStyleClass().add("add-button");
        addPersonBtn.setOnAction(e -> new PersonAddView().showForm(this::loadPeople));
        
        HBox buttonPanel = new HBox(10);
        buttonPanel.getStyleClass().add("button-panel");
        buttonPanel.getChildren().addAll(addPersonBtn);
        
        layout.getChildren().addAll(searchField, table, buttonPanel);
        return layout;
    }
}