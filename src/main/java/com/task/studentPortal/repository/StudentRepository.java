package com.task.studentPortal.repository;

import com.task.studentPortal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEnrollmentNumberOrEmail(String enrollmentNumber, String email);

    Optional<Student> findByForgotPasswordToken(String token);

    Optional<Student> findByEmail(String email);

}
