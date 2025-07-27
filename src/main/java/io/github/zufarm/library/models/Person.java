package io.github.zufarm.library.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "full_name")
	@NotBlank(message = "ФИО не может быть пустым")
    @Size(min = 3, max = 100, message = "ФИО должно быть от 3 до 100 символов")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+(?:[- ][А-ЯЁ][а-яё]+)* [А-ЯЁ][а-яё]+(?: [А-ЯЁ][а-яё]+)?$", message = "ФИО содержит недопустимые символы")
	private String fullName;
	
	@Column(name = "birth_year")
	@Min(value = 1900, message = "Год рождения должен быть не меньше 1900")
    @Max(value = 2025, message = "Год рождения должен быть не больше 2025")
	private int birthYear;
	
	@OneToMany(mappedBy = "bookHolder", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	private List<Book> books = new ArrayList<>();
	
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	// For @ModelAttribute
	public Person() {
		
	}
	public Person(String fullName, int birthYear) {
		this.fullName = fullName;
		this.birthYear = birthYear;
	}
	
	public void addBook(Book book) {
		books.add(book);
		book.setBookHolder(this);
	}
	
	public void returnBook(Book book) {
		books.remove(book);
		book.setBookHolder(null);
	}
	
}
