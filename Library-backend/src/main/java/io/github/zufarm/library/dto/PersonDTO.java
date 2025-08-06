package io.github.zufarm.library.dto;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PersonDTO {
	
	@NotBlank(message = "ФИО не может быть пустым")
    @Size(min = 3, max = 100, message = "ФИО должно быть от 3 до 100 символов")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+(?:[- ][А-ЯЁ][а-яё]+)* [А-ЯЁ][а-яё]+(?: [А-ЯЁ][а-яё]+)?$", message = "ФИО содержит недопустимые символы")
	private String fullName;
	
	private int id;
	
	private String phoneNumber;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
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
}
