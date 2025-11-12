package hemen.go;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "passSegura12345";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}