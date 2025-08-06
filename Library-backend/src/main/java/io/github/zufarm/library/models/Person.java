package io.github.zufarm.library.models;
import java.time.LocalDate;
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
	
	@OneToMany(mappedBy = "bookHolder", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	private List<Book> books = new ArrayList<>();
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "birth_date")
	private LocalDate birthDate;
	
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
	
	public Person() {
		
	}
	public Person(String fullName, String phoneNumber, LocalDate birthDate) {
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
	}
	
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
	public void addBook(Book book) {
		books.add(book);
		book.setBookHolder(this);
	}
	
	public void returnBook(Book book) {
		books.remove(book);
		book.setBookHolder(null);
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", fullName=" + fullName + ", books=" + books + ", phoneNumber=" + phoneNumber
				+ ", birthDate=" + birthDate + "]";
	}
	
	
}
