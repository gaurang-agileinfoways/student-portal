package com.task.studentPortal.utils.tokenGeneration;

import com.task.studentPortal.model.Student;
import com.task.studentPortal.utils.constants.ApplicationConstant;
import com.task.studentPortal.utils.dtos.auth.LoginRequestDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AuthException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final Environment env;

    public String generateToken(LoginRequestDto body, Student student) {
        Authentication unAuth = UsernamePasswordAuthenticationToken.unauthenticated(body.getEnrollmentOrEmail(), body.getPassword());
        Authentication auth = authenticationManager.authenticate(unAuth);
        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthException("Invalid username password!");
        }

        //  Get secret from environment variable
        String jwtSecret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY, ApplicationConstant.JWT_SECRET_DEFAULT);

        //  Create secret key for further process of jwtSecret
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("Spring security")
                .setSubject("Learning spring security")
                .claim("email", student.getEmail())
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 36000000))
                .signWith(secretKey).compact();
    }
}
