package io.github.zufarm.library.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name = "App_user")
public class AppUser {
	    @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @NotEmpty(message = "Имя не должно быть пустым")
	    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
	    @Column(name = "username")
	    private String username;

	    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
	    @Column(name = "year_of_birth")
	    private int yearOfBirth;

	    @Column(name = "password")
	    private String password;
	    
	    @Column(name = "role")
	    private String role;

	    public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public AppUser() {
	    }

	    public AppUser(String username, int yearOfBirth) {
	        this.username = username;
	        this.yearOfBirth = yearOfBirth;
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public int getYearOfBirth() {
	        return yearOfBirth;
	    }

	    public void setYearOfBirth(int yearOfBirth) {
	        this.yearOfBirth = yearOfBirth;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

		@Override
		public String toString() {
			return "AppUser [id=" + id + ", username=" + username + ", yearOfBirth=" + yearOfBirth + ", password="
					+ password + "]";
		}

	}
