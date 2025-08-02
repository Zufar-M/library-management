package io.github.zufarm.library.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookDTO {
    private final SimpleStringProperty name;
    private final SimpleStringProperty author;
    private final SimpleIntegerProperty year;

    @JsonCreator
    public BookDTO(
            @JsonProperty("name") String name,
            @JsonProperty("author") String author,
            @JsonProperty("year") int year) {
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.year = new SimpleIntegerProperty(year);
    }

    public String getName() {
        return name.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public int getYear() {
        return year.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }
    
    public void setName(String name) {
        this.name.set(name);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setYear(int year) {
        this.year.set(year);
    }
    
}