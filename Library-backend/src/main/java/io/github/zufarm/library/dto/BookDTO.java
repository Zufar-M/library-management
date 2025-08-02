package io.github.zufarm.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookDTO {
	
	@NotBlank(message = "Название книги не может быть пустым")
	@Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
	private String name;
	
	@NotBlank(message = "Поле автор не может быть пустым")
	@Size(min = 1, max = 100, message = "Имя автора должно быть от 1 до 100 символов")
	private String author;
	
	@Min(value = 1000, message = "Год издания должен быть не меньше 1000")
    @Max(value = 2100, message = "Год издания должен быть не больше 2100")
	private int year;

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
	
	
}
