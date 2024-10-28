package com.task.studentPortal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "monthly_attendance")
public class MonthlyAttendance {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @JsonIgnore
    Student student;

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer totalDay;

    @Column(nullable = false)
    private Long attendedDay;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public MonthlyAttendance(String status, Integer totalDay, Long attendedDay, Date startDate, Date endDate, Student student) {
        this.status = status;
        this.totalDay = totalDay;
        this.attendedDay = attendedDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.student = student;
    }
}
