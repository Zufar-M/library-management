package io.github.zufarm.library.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PersonDTO {
    private final SimpleStringProperty fullName;
    private final SimpleIntegerProperty birthYear;

    @JsonCreator
    public PersonDTO(
            @JsonProperty("fullName") String fullName,
            @JsonProperty("birthYear") int birthYear) {
        this.fullName = new SimpleStringProperty(fullName);
        this.birthYear = new SimpleIntegerProperty(birthYear);
    }

    public String getFullName() {
        return fullName.get();
    }

    public int getBirthYear() {
        return birthYear.get();
    }
    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public SimpleIntegerProperty birthYearProperty() {
        return birthYear;
    }
    
    public void setName(String fullName) {
        this.fullName.set(fullName);
    }

    public void Year(int birthYear) {
        this.birthYear.set(birthYear);
    }
}