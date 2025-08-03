package io.github.zufarm.library.view.person;
import io.github.zufarm.library.services.PersonService;
import io.github.zufarm.library.dto.PersonDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PersonListView {
    private final PersonService personService = new PersonService();
    private final TableView<PersonDTO> table = new TableView<>();
    private final ObservableList<PersonDTO> people = FXCollections.observableArrayList();
    
    public PersonListView() {
        initializeTable();
        loadPeople();
    }
    
    private void initializeTable() {
        TableColumn<PersonDTO, String> fullNameCol = new TableColumn<>("Имя");
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<PersonDTO, Integer> birthYearCol = new TableColumn<>("Год рождения");
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        
        table.getColumns().addAll(fullNameCol, birthYearCol);
        table.setItems(people);
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
        } catch (Exception e) {
            e.printStackTrace();
            people.clear();
        }
    }
    
    public Parent getView() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        
        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> loadPeople());
        
        Button addPersonBtn = new Button("Добавить нового читателя");
        addPersonBtn.setOnAction(e -> {
            new PersonAddView().showForm(this::loadPeople);
        });
        HBox buttonPanel = new HBox(10);
        buttonPanel.getChildren().addAll(refreshBtn, addPersonBtn);
        
        layout.getChildren().addAll(table, buttonPanel);
        return layout;
    }
}