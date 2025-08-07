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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotBlank(message = "Название книги не может быть пустым")
    @Size(max = 255, message = "Название книги не должно превышать 255 символов")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Автор не может быть пустым")
    @Size(max = 255, message = "Имя автора не должно превышать 255 символов")
    @Column(nullable = false)
    private String author;
    
    @NotNull(message = "Год издания обязателен")
    @Min(value = 1, message = "Год издания должен быть положительным числом")
    @Max(value = 2100, message = "Год издания не может быть больше 2100")
    @Column(nullable = false)
    private Integer year;
    
    @Size(max = 100, message = "Жанр не должен превышать 100 символов")
    private String genre;
    
    @Size(max = 50, message = "Язык не должен превышать 50 символов")
    private String language = "Russian";	
    
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id", nullable = true)
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
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
