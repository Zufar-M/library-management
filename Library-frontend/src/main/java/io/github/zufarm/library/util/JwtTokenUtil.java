package io.github.zufarm.library.util;

import io.github.zufarm.library.dto.LoginResponse;

public class JwtTokenUtil {
private static String token;
private static String role;

    public static void setToken(String jwtToken) {
        token = jwtToken;
    }
    
    public static String getToken() {
        return token;
    }
    
    public static void clearToken() {
        token = null;
    }

	public static String getRole() {
		return role;
	}

	public static void setRole(String role) {
		JwtTokenUtil.role = role;
	}
	
	public static void initValues(LoginResponse response) {
		token = response.getToken();
		role = response.getRole();
	}
    
}

