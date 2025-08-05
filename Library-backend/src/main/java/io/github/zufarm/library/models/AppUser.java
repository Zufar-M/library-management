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
	    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов длиной")
	    @Column(name = "username")
	    private String username;

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
