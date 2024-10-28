package com.task.studentPortal.repository;


import com.task.studentPortal.model.MonthlyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyAttendanceRepository extends JpaRepository<MonthlyAttendance, Long> {

    List<MonthlyAttendance> findByStudentId(Long studentId);
}