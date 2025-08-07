package io.github.zufarm.library.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name = "app_user")
public class AppUser {
	    @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @NotBlank(message = "Имя пользователя не может быть пустым")
	    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
	    @Column(nullable = false, unique = true)
	    private String username;

	    @NotBlank(message = "Пароль не может быть пустым")
	    @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
	    @Column(nullable = false)
	    private String password;

	    @NotBlank(message = "Роль не может быть пустой")
	    @Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "Роль должна быть либо USER, либо ADMIN")
	    @Column(nullable = false)
	    private String role;

	    public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public AppUser() {
	    }

	    public AppUser(String username) {
	        this.username = username;
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

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
	}
