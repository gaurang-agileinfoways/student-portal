package com.task.studentPortal.config.filter;

import com.google.gson.Gson;
import com.task.studentPortal.config.StudentUserDetails;
import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.utils.constants.ApplicationConstant;
import com.task.studentPortal.utils.responseHandler.ErrorResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final StudentRepository studentRepository;

    @Autowired
    public JwtTokenValidatorFilter(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        System.out.println("STUDENT REPO: " + studentRepository);
        String jwtToken = request.getHeader(ApplicationConstant.JWT_HEADER);
        try {
            if (jwtToken == null) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token!!");
                return;
            }

            Environment env = getEnvironment();
            String jwtSecret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY, ApplicationConstant.JWT_SECRET_DEFAULT);
            JwtParser parser = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build();
            Claims claims = parser.parseClaimsJws(jwtToken).getBody();

            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));

            Optional<Student> optStudent = studentRepository.findByEmail(email);

            if (optStudent.isEmpty()) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "User not found.");
            }

            Student student = optStudent.get();
            StudentUserDetails userDetails = new StudentUserDetails(
                    AuthorityUtils.commaSeparatedStringToAuthorityList("USER"),
                    student.getId(), student.getEmail(), student.getName(), student.getPassword(),
                    student.getEnrollmentNumber(), student.getEnrollmentNumber());

            // Create the Authentication object
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "Invalid JWT signature or malformed token.");
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "JWT claims string is empty.");
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(new Gson().toJson(new ErrorResponse(status.getReasonPhrase(), message, status.value())));
        response.flushBuffer();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/auth/");
    }
}



