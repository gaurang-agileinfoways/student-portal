package com.task.studentPortal.service;

import com.task.studentPortal.model.Attendance;
import com.task.studentPortal.model.MonthlyAttendance;
import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.AttendanceRepository;
import com.task.studentPortal.repository.MonthlyAttendanceRepository;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.utils.constants.AttendanceConstant;
import com.task.studentPortal.utils.dtos.attendance.DBAttendanceDto;
import com.task.studentPortal.utils.dtos.attendance.DBGetAttendanceCountDto;
import com.task.studentPortal.utils.dtos.attendance.DateAttendanceDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AlreadyExistException;
import com.task.studentPortal.utils.exceptions.customExceptions.AuthException;
import com.task.studentPortal.utils.exceptions.customExceptions.NotFoundException;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import com.task.studentPortal.utils.services.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AttendanceService {

    private AttendanceRepository attendanceRepository;
    private StudentRepository studentRepository;
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    public ResponseEntity<?> getMyAttendance(DateAttendanceDto body, Long id) {

        if (body.getStartDate() == null || body.getEndDate() == null) {
            LocalDateTime startDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            Timestamp startDate = Timestamp.valueOf(startDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime());

            LocalDateTime endDateTime = startDateTime.plusHours(24);
            Timestamp endDate = Timestamp.valueOf(endDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime());

            body.setStartDate(startDate);
            body.setEndDate(endDate);
        }

        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(body.getStartDate(), body.getEndDate(), id);
        return ResponseHandler.generateResponse(attendance, "Attendance fetched success");
    }

    public ResponseEntity<?> addMyAttendance(Long id) {

        LocalDateTime startDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Timestamp startDate = Timestamp.valueOf(startDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime());

        // Check if today is Saturday or Sunday
        DayOfWeek dayOfWeek = startDateTime.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new NotAcceptableStatusException("Attendance cannot be added on weekends. ");
        }

        LocalDateTime endDateTime = startDateTime.plusHours(24);
        Timestamp endDate = Timestamp.valueOf(endDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime());

        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(startDate, endDate, id);
        if (!attendance.isEmpty()) {
            throw new AlreadyExistException("Attendance already added");
        }

        Student student = studentRepository.findById(id).orElseThrow(() -> new AuthException("Student not found."));

        Attendance attend = new Attendance();
        attend.setDate(new Date());
        attend.setStatus(AttendanceConstant.PRESENT);
        attend.setStudent(student);
        return ResponseHandler.generateResponse(attendanceRepository.save(attend), "Attendance added success");
    }

    public ResponseEntity<?> getGetAttendance(Long studentId) {
        return ResponseHandler.generateResponse(monthlyAttendanceRepository.findByStudentId(studentId), "Student attendance fetched.");
    }

    // execute every month last date
    @Scheduled(cron = "0 59 23 L * ?")
//    @Scheduled(cron = "0 * * * * *")
    private void attendanceMonthlySchedule() {
        Calendar calendar = Calendar.getInstance();

        // Set to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1); // Move to last month

        // Get start of last month
        LocalDateTime startLocalDateTime = LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, // Months are 0-based in Calendar
                1, 0, 0);
        Timestamp startDate = Timestamp.valueOf(startLocalDateTime);

        // Set to the last day of last month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Get end of last month (end of the day)
        LocalDateTime endLocalDateTime = LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Timestamp endDate = Timestamp.valueOf(endLocalDateTime);

        // Example for counting working days (assuming this method exists)
        Integer workingDays = CommonService.countWorkingDays(calendar);

       List<DBGetAttendanceCountDto> attendance = attendanceRepository.getAttendance(startDate, endDate);

        System.out.println(attendance);
        Set<MonthlyAttendance> monthlyAttendances = new HashSet<>();
        for (DBGetAttendanceCountDto attendances : attendance) {
            long avg = (attendances.getAttendedDays() * 100) / workingDays;

            String attendanceResult = avg >= 90 && avg <= 100 ? "5*" : avg >= 75 && avg < 90 ? "4*" : avg >= 29 && avg < 75 ? "PASS" : "FAIL";

            Student s = studentRepository.findById(attendances.getStudentId()).orElseThrow(() -> new NotFoundException("Student not found"));
            MonthlyAttendance ma = new MonthlyAttendance(attendanceResult, workingDays, attendances.getAttendedDays(), startDate, endDate, s);
            monthlyAttendances.add(ma);
        }

        monthlyAttendanceRepository.saveAll(monthlyAttendances);
        Logger.getLogger("Attendance Cron successfully.");
    }
}
