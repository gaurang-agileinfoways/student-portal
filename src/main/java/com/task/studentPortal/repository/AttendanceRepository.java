package com.task.studentPortal.repository;

import com.task.studentPortal.model.Attendance;
import com.task.studentPortal.utils.dtos.attendance.DBAttendanceDto;
import com.task.studentPortal.utils.dtos.attendance.DBGetAttendanceCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    //    @Query("SELECT a FROM Attendance a WHERE a.date >= :startDate AND a.date <= :endDate AND a.student.id = :studentId")
    @Query("SELECT new com.task.studentPortal.utils.dtos.attendance.DBAttendanceDto(a.id AS id, a.student.id AS studentId, a.date AS date, a.status AS status) " +
            "FROM Attendance a " +
            "WHERE a.date >= :startDate AND a.date <= :endDate AND a.student.id = :studentId")
    List<DBAttendanceDto> findByStartDateAndEndDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("studentId") Long studentId);

    @Query("SELECT new com.task.studentPortal.utils.dtos.attendance.DBGetAttendanceCountDto(" +
            "student.id, count(id)) FROM Attendance a " +
            "WHERE a.date >= :startDate AND a.date <= :endDate " +
            "group by student.id")
    List<DBGetAttendanceCountDto> getAttendance(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
