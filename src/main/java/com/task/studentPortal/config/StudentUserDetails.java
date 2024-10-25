package com.task.studentPortal.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class StudentUserDetails implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private Long id;
    private String email;
    private String name;
    private String password;
    private String username;
    private String enrollmentNumber;

    public StudentUserDetails(List<GrantedAuthority> authorities, Long id, String email, String name, String password, String username, String enrollmentNumber) {
        this.authorities = authorities;
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.username = username;
        this.enrollmentNumber = enrollmentNumber;
    }
}