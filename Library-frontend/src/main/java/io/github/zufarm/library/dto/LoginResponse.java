package io.github.zufarm.library.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String error;

    public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public LoginResponse(String token, String role, String error) {
		super();
		this.token = token;
		this.role = role;
		this.error = error;
	}
	public LoginResponse() {
		
	}
	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}