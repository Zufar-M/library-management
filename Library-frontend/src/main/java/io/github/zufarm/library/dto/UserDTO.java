package io.github.zufarm.library.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
	private int id;
    private String username;
    private String password;
    private int yearOfBirth;
    private String role;

    @JsonCreator
    public UserDTO(
    		@JsonProperty("id") int id,
            @JsonProperty("username") String username,
            @JsonProperty("yearOfBirth") int yearOfBirth,
            @JsonProperty("role") String role,
    		@JsonProperty("password") String password){
    	this.id = id;
        this.username = username;
        this.yearOfBirth = yearOfBirth;
        this.role = role;
        this.password = password;
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

	

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    
    
    
}