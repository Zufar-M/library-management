package io.github.zufarm.library.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookDTO {
	
	private int id;
	
	@NotBlank(message = "Название книги не может быть пустым")
    @Size(max = 255, message = "Название книги не должно превышать 255 символов")
    private String name;
    
    @NotBlank(message = "Автор не может быть пустым")
    @Size(max = 255, message = "Имя автора не должно превышать 255 символов")
    private String author;
    
    @NotNull(message = "Год издания обязателен")
    @Min(value = 1, message = "Год издания должен быть положительным числом")
    @Max(value = 2100, message = "Год издания не может быть больше 2100")
    private Integer year;
    
    @Size(max = 100, message = "Жанр не должен превышать 100 символов")
    private String genre;
    
    @Size(max = 50, message = "Язык не должен превышать 50 символов")
    private String language = "Russian";	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
