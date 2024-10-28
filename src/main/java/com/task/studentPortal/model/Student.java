package com.task.studentPortal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "student")
public class Student {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, updatable = false, length = 12)
    private String enrollmentNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String profilePicUrl;

    @Column(nullable = false)
    private Integer standard;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Column(nullable = true, columnDefinition = "", unique = true)
    private String forgotPasswordToken;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Mark> marks;
}
