package io.github.zufarm.library.util;

public class JwtTokenUtil {
    private static String token;
    
    public static void setToken(String jwtToken) {
        token = jwtToken;
    }
    
    public static String getToken() {
        return token;
    }
    
    public static void clearToken() {
        token = null;
    }
}