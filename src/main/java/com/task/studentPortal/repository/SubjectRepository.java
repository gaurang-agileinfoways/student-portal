package com.task.studentPortal.repository;

import com.task.studentPortal.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByNameAndStandard(String name, Integer standard);
}
