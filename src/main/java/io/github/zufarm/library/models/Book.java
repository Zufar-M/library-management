package io.github.zufarm.library.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	@NotBlank(message = "Название книги не может быть пустым")
	@Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
	private String name;
	
	@Column(name = "author")
	@NotBlank(message = "Поле автор не может быть пустым")
	@Size(min = 1, max = 100, message = "Имя автора должно быть от 1 до 100 символов")
	private String author;
	
	@Column(name = "year")
	@Min(value = 1000, message = "Год издания должен быть не меньше 1000")
    @Max(value = 2100, message = "Год издания должен быть не больше 2100")
	private int year;
	
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	private Person bookHolder;
	
	public Person getBookHolder() {
		return bookHolder;
	}
	public void setBookHolder(Person bookHolder) {
		this.bookHolder = bookHolder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Book(String name, String author, int year) {
		this.name = name;
		this.author = author;
		this.year = year;
	}
	public Book() {
		
	}
}
