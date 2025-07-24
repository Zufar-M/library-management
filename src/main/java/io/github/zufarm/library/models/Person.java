package io.github.zufarm.library.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Person {
	private int id;
	
	@NotBlank(message = "ФИО не может быть пустым")
    @Size(min = 5, max = 100, message = "ФИО должно быть от 3 до 100 символов")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+(?:[- ][А-ЯЁ][а-яё]+)* [А-ЯЁ][а-яё]+(?: [А-ЯЁ][а-яё]+)?$", message = "ФИО содержит недопустимые символы")
	private String fullName;
	
	@Min(value = 1900, message = "Год рождения должен быть не меньше 1900")
    @Max(value = 2025, message = "Год рождения должен быть не больше 2025")
	private int birthYear;
	
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
	public Person(int id, String fullName, int birthYear) {
		this.id = id;
		this.fullName = fullName;
		this.birthYear = birthYear;
	}
	
}
