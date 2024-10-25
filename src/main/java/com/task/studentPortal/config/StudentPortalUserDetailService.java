package com.task.studentPortal.config;

import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class StudentPortalUserDetailService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public StudentUserDetails loadUserByUsername(String enrollmentOrEmail) throws UsernameNotFoundException {
        // You can choose to load by email or enrollment number
        Student student = studentRepository.findByEnrollmentNumberOrEmail(enrollmentOrEmail, enrollmentOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + enrollmentOrEmail));

        return new StudentUserDetails(
                getGrantedAuthorities(),
                student.getId(),
                student.getEmail(),
                student.getName(),
                student.getPassword(),
                student.getEnrollmentNumber(),
                student.getEnrollmentNumber()
        );
    }

    private List<GrantedAuthority> getGrantedAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

}
