package com.task.studentPortal.service;

import com.task.studentPortal.model.Attendance;
import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.AttendanceRepository;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.utils.constants.AttendanceConstant;
import com.task.studentPortal.utils.dtos.attendance.DBAttendanceDto;
import com.task.studentPortal.utils.dtos.attendance.DateAttendanceDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AlreadyExistException;
import com.task.studentPortal.utils.exceptions.customExceptions.AuthException;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AttendanceService {

    private AttendanceRepository attendanceRepository;
    private StudentRepository studentRepository;

    public ResponseEntity<?> getMyAttendance(DateAttendanceDto body, Long id) {

        System.out.println(body);
        if (body.getStartDate() == null && body.getEndDate() == null) {
            LocalDateTime startDateTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
            body.setStartDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));

            LocalDateTime endDateTime = startDateTime.plusHours(24);
            body.setEndDate(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }

        System.out.println("BODY: " + body);

        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(body.getStartDate(), body.getEndDate(), id);
        return ResponseHandler.generateResponse(attendance, "Attendance fetched success");
    }

    public ResponseEntity<?> addMyAttendance(Long id) {
        LocalDateTime startDateTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime endDateTime = startDateTime.plusHours(24);
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());


        System.out.println(startDate + " --  " + endDate);
        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(startDate, endDate, id);

        if (!attendance.isEmpty()) {
            throw new AlreadyExistException("Attendance already added");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AuthException("Student not found."));

        Attendance attend = new Attendance();
        attend.setDate(new Date());
        attend.setStatus(AttendanceConstant.PRESENT);
        attend.setStudent(student);
        return ResponseHandler.generateResponse(attendanceRepository.save(attend), "Attendance added success");
    }
}
