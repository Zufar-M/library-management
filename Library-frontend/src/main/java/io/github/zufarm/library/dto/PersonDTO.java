package io.github.zufarm.library.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PersonDTO {
    private final int id;
    private final SimpleStringProperty fullName;
    private final SimpleStringProperty phoneNumber;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final SimpleObjectProperty<LocalDate> birthDate;
    
    
    @JsonCreator
    public PersonDTO(
            @JsonProperty("id") int id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("birthDate") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
            @JsonProperty("phoneNumber") String phoneNumber) {
        this.id = id;
        this.fullName = new SimpleStringProperty(fullName);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    public int getId() {
        return id;
    }
    
    public String getFullName() {
        return fullName.get();
    }
    
    public LocalDate getBirthDate() {
        return birthDate.get();
    }
    
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }
    
    public SimpleObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }
    
    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }
}