package com.task.studentPortal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "subject")
public class Subject {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Integer standard;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Mark> marks;
}
