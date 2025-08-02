package io.github.zufarm.library.view.person;
import io.github.zufarm.library.services.PersonService;
import io.github.zufarm.library.dto.PersonDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        TableColumn<PersonDTO, String> titleCol = new TableColumn<>("Имя");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<PersonDTO, Integer> yearCol = new TableColumn<>("Год рождения");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        
        table.getColumns().addAll(titleCol, yearCol);
        table.setItems(people);
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
        
        layout.getChildren().addAll(table, refreshBtn);
        return layout;
    }
}